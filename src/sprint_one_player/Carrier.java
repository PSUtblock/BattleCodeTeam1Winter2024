package sprint_one_player;

import battlecode.common.*;

import java.util.HashSet;
import java.util.Set;

import static sprint_one_player.RobotPlayer.*;

public class Carrier {
    // Map locations to store headquarters, closest well, and island positions.
    private static MapLocation hqLocation;
    private static MapLocation wellLocation;
    private static MapLocation islandLocation;
    private static MapLocation myLocation;
//    private static boolean recordedHQ = false;

    /**
     * Run a single turn for a Carrier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runCarrier(RobotController rc) throws GameActionException {
        Direction randDir = directions[rng.nextInt(directions.length)]; // Hold rand direction if needed.
        myLocation = rc.getLocation();                                  // Get robot's current location.

        // Try to write location of the headquarters to the communications array (robot spawns at HQ).
//        if (!recordedHQ) {
//            writeHQ(rc);
//            recordedHQ = true;
//        }


        // If the headquarters have not already been found, locate the closest one.
//        hqLocation = readHQ(rc);
        if (hqLocation == null) {
            locateHQ(rc);
        }

        // If the closest well has not been found, locate it.
        if (wellLocation == null) {
            locateWell(rc);
        }

        // If the closest island has not been found, locate it.
        if (islandLocation == null) {
            locateIsland(rc);
        }

        // If the robot does not have an anchor, try to collect one.
        if (rc.getAnchor() == null) {
            collectAnchor(rc);
        }

        // If the robot has an anchor singularly focus on getting it to the first island it sees.
        if (rc.getAnchor() != null && islandLocation != null) {
            rc.setIndicatorString("Moving my anchor towards " + islandLocation);
            while (!myLocation.equals(islandLocation)) {
                moveToLocation(rc, myLocation.directionTo(islandLocation));
            }
            if (rc.canPlaceAnchor()) {
                rc.placeAnchor();
                rc.setIndicatorString("Huzzah, placed anchor!");
                islandLocation = null;
            }
        }

        // Try to gather from squares around us.
        collectFromWell(rc);

        // If next to headquarters, deposit everything from the carrier.
        depositResource(rc, ResourceType.MANA);
        depositResource(rc, ResourceType.ADAMANTIUM);

        // Depending on certain health level, think about attacking.
//        if (rc.getHealth() < 150) {
//            RobotInfo[] enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
//            if (enemyRobots.length > 0) {
//                RobotInfo enemyToAttack = enemyRobots[0];
//                int minHealth = enemyToAttack.getHealth();
//                // Find the enemy with the lowest health.
//                for (int i = 1; i < enemyRobots.length; ++i) {
//                    if (minHealth > enemyRobots[i].getHealth()) {
//                        enemyToAttack = enemyRobots[i];
//                        minHealth = enemyToAttack.getHealth();
//                    }
//                }
//                // Try to attack the enemy.
//                if (rc.canAttack(enemyToAttack.location)) {
//                    rc.attack(enemyToAttack.location);
//                }
//            }
//        }

        // If the robot has capacity, move toward a nearby well.
        if (rc.getWeight() < GameConstants.CARRIER_CAPACITY) {
            locateWell(rc);
            if (wellLocation != null) {
                // Only move if not adjacent to a well.
                if (!myLocation.isAdjacentTo(wellLocation))
                    moveToLocation(rc, myLocation.directionTo(wellLocation));
            }
            else
                moveToLocation(rc, randDir);
        }
        else if (hqLocation != null) {
            // Only move if not adjacent to HQ.
            if (!myLocation.isAdjacentTo(hqLocation))
                moveToLocation(rc, myLocation.directionTo(hqLocation));
        }
        else
            // Otherwise, just move randomly.
            moveToLocation(rc, randDir);
    }

    /** Locate the Headquarters, so the Carrier can always find its way back. **/
    public static void locateHQ(RobotController rc) throws GameActionException {
        RobotInfo[] robots = rc.senseNearbyRobots();

        // Check for this teams Headquarters and record its location.
        for (RobotInfo robot : robots) {
            if ((robot.getTeam() == rc.getTeam()) && (robot.getType() == RobotType.HEADQUARTERS)) {
                hqLocation = robot.getLocation();
                rc.setIndicatorString("Found headquarters at: " + hqLocation);
                break;
            }
        }
    }

    /** Locate the closest well based on current robot's location. **/
    public static void locateWell(RobotController rc) throws GameActionException {
        WellInfo[] wells = rc.senseNearbyWells();
        // Find the closest or only well nearby.
        if (wells.length > 0) {
            MapLocation[] wellLocations = new MapLocation[wells.length];
            for (int i = 0; i < wellLocations.length; ++i) {
                wellLocations[i] = wells[i].getMapLocation();
            }
            wellLocation = getClosestLocation(rc, wellLocations);
            rc.setIndicatorString("Found closest well at: " + wellLocation);
        }
    }

    /** Locate closest unoccupied island nearby. **/
    public static void locateIsland(RobotController rc) throws GameActionException {
        int[] islands = rc.senseNearbyIslands();
        Set<MapLocation> closestIslands = new HashSet<>();
        for (int id : islands) {
            // Check if island is unoccupied first.
            if (rc.senseTeamOccupyingIsland(id) == Team.NEUTRAL) {
                MapLocation[] thisIslandLocs = rc.senseNearbyIslandLocations(id);
                // Add the closest parts of each island.
                if (thisIslandLocs.length > 0) {
                    closestIslands.add(getClosestLocation(rc, thisIslandLocs));
                }
            }
        }
        // Get the actual closest part of the closest island.
        if (!closestIslands.isEmpty()) {
            islandLocation = getClosestLocation(rc, closestIslands);
        }
    }

    /** Return the closest location with respect to robot's current location. Uses an array of MapLocations as a parameter. **/
    public static MapLocation getClosestLocation(RobotController rc, MapLocation[] locations) throws GameActionException {
        if (locations.length > 0) {
            MapLocation currClosest = locations[0];
            int minDistance = myLocation.distanceSquaredTo(currClosest);

            for (MapLocation island : locations) {
                int currDistance = myLocation.distanceSquaredTo(island);
                if (minDistance > currDistance) {
                    minDistance = currDistance;
                    currClosest = island;
                }
            }
            return currClosest;
        }

        return null;
    }

    /** Return the closest location with respect to robot's current location. Uses set of MapLocations as a parameter. **/
    public static MapLocation getClosestLocation(RobotController rc, Set<MapLocation> locations) throws GameActionException {
        if (!locations.isEmpty()) {
            MapLocation currClosest = locations.iterator().next();
            int minDistance = myLocation.distanceSquaredTo(currClosest);

            for (MapLocation island : locations) {
                int currDistance = myLocation.distanceSquaredTo(island);
                if (minDistance > currDistance) {
                    minDistance = currDistance;
                    currClosest = island;
                }
            }
            return currClosest;
        }

        return null;
    }

    /** Collect from well in adjacent squares. **/
    public static void collectFromWell(RobotController rc) throws GameActionException {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation adjacentLoc = new MapLocation(myLocation.x + dx, myLocation.y + dy);
                if (rc.canCollectResource(adjacentLoc, -1)) {
                    rc.collectResource(adjacentLoc, -1);
                    rc.setIndicatorString("Collecting, now have, AD:" +
                            rc.getResourceAmount(ResourceType.ADAMANTIUM) +
                            " MN: " + rc.getResourceAmount(ResourceType.MANA) +
                            " EX: " + rc.getResourceAmount(ResourceType.ELIXIR));
                }
            }
        }
    }

    /** Collect an anchor from headquarters. **/
    public static void collectAnchor(RobotController rc) throws GameActionException {
        if (hqLocation != null && rc.canTakeAnchor(hqLocation, Anchor.STANDARD)) {
            rc.takeAnchor(hqLocation, Anchor.STANDARD);
            rc.setIndicatorString("Taking anchor, now have, Anchor: " + rc.getAnchor());
        }
        else {
            rc.setIndicatorString("Unable to collect anchor.");
        }
    }

    /** Deposit all resources of a certain type to headquarters. **/
    public static void depositResource(RobotController rc, ResourceType type) throws GameActionException {
        int amount = rc.getResourceAmount(type);

        // If robot has any resources, deposit all of it.
        if ((amount > 0) && hqLocation != null && rc.canTransferResource(hqLocation, type, amount)) {
            rc.transferResource(hqLocation, type, amount);
            rc.setIndicatorString("Depositing, now have, AD:" +
                    rc.getResourceAmount(ResourceType.ADAMANTIUM) +
                    " MN: " + rc.getResourceAmount(ResourceType.MANA) +
                    " EX: " + rc.getResourceAmount(ResourceType.ELIXIR));
        }
    }

    /** Move the robot in a given direction. **/
    public static void moveToLocation(RobotController rc, Direction dir) throws GameActionException {
        Direction randDir = directions[rng.nextInt(directions.length)];
        // If the robot can move, move in that direction. Otherwise, try to move randomly.
        if (rc.canMove(dir)) {
            rc.move(dir);
            myLocation = rc.getLocation();
        }
        else if (rc.canMove(randDir)) {
            rc.move(randDir);
            myLocation = rc.getLocation();
        }
    }
}