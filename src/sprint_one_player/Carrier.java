package sprint_one_player;

import battlecode.common.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static sprint_one_player.RobotPlayer.directions;
import static sprint_one_player.RobotPlayer.rng;

public class Carrier {
    // Map locations to store headquarters, closest well, and island positions.
    private static MapLocation hqLocation;
    private static MapLocation wellLocation;
    private static Set<MapLocation> islandLocs = new HashSet<>();

    /**
     * Run a single turn for a Carrier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runCarrier(RobotController rc) throws GameActionException {
        Direction randDir = directions[rng.nextInt(directions.length)]; // Hold rand direction if needed.
        MapLocation me = rc.getLocation();                              // Get robot's current location.

        // If the headquarters have not already been found, locate it.
        if (hqLocation == null)
            locateHQ(rc);

        // If the robot does not have an anchor, try to collect one.
        if (rc.getAnchor() == null) {
            collectAnchor(rc);
        }

        // If the robot has an anchor singularly focus on getting it to the first island it sees.
        if (rc.getAnchor() != null) {
            locateIslands(rc);
            if (!islandLocs.isEmpty()) {
                MapLocation islandLocation = islandLocs.iterator().next();
                rc.setIndicatorString("Moving my anchor towards " + islandLocation);
                while (!me.equals(islandLocation)) {
                    moveToLocation(rc, me.directionTo(islandLocation));
                    me = rc.getLocation();
                }
                if (rc.canPlaceAnchor()) {
                    rc.placeAnchor();
                    rc.setIndicatorString("Huzzah, placed anchor!");
                }
            }
        }
        // Try to gather from squares around us.
        gatherAdjacentSquares(rc, me);

        // If next to headquarters, deposit everything from the carrier.
        depositResource(rc, ResourceType.MANA);
        depositResource(rc, ResourceType.ADAMANTIUM);

        // If the robot has almost no health left, think about attacking.
        if (rc.getHealth() < 5) {
            RobotInfo[] enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
            // Only try to attack if there are a lot of nearby enemies.
            if (enemyRobots.length >= 8) {
                RobotInfo enemyToAttack = enemyRobots[0];
                int minHealth = enemyToAttack.getHealth();
                // Find the enemy with the lowest health.
                for (int i = 1; i < enemyRobots.length; ++i) {
                    if (minHealth > enemyRobots[i].getHealth()) {
                        enemyToAttack = enemyRobots[i];
                        minHealth = enemyToAttack.getHealth();
                    }
                }
                // Try to attack the enemy.
                if (rc.canAttack(enemyToAttack.location)) {
                    rc.attack(enemyToAttack.location);
                }
            }
        }

        // If the robot has capacity, move toward a nearby well.
        if (rc.getWeight() < GameConstants.CARRIER_CAPACITY) {
            locateWell(rc, me);
            if (wellLocation != null) {
                // Only move if not adjacent to a well.
                if (!me.isAdjacentTo(wellLocation))
                    moveToLocation(rc, me.directionTo(wellLocation));
            }
            else
                moveToLocation(rc, randDir);
        }
        else if (hqLocation != null) {
            // Only move if not adjacent to HQ.
            if (!me.isAdjacentTo(hqLocation))
                moveToLocation(rc, me.directionTo(hqLocation));
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
    public static void locateWell(RobotController rc, MapLocation me) throws GameActionException {
        WellInfo[] wells = rc.senseNearbyWells();

        // Find the closest or only well nearby.
        if (wells.length > 1) {
            WellInfo closestWell = wells[0];
            int minDistance = me.distanceSquaredTo(closestWell.getMapLocation());

            for (WellInfo well : wells) {
                int wellDistance = me.distanceSquaredTo(well.getMapLocation());
                if (minDistance > wellDistance) {
                    minDistance = wellDistance;
                    closestWell = well;
                }
            }
            wellLocation = closestWell.getMapLocation();
            rc.setIndicatorString("Found closest well at: " + wellLocation);
        }
        else if (wells.length == 1) {
            wellLocation = wells[0].getMapLocation();
            rc.setIndicatorString("Found well at: " + wellLocation);
        }
        else {
            rc.setIndicatorString("No wells are nearby.");
        }
    }

    /** Locate all unoccupied islands nearby. **/
    public static void locateIslands(RobotController rc) throws GameActionException {
        int[] islands = rc.senseNearbyIslands();
        for (int id : islands) {
            // Check if island is unoccupied first.
            if (rc.senseTeamOccupyingIsland(id) == Team.NEUTRAL) {
                MapLocation[] thisIslandLocs = rc.senseNearbyIslandLocations(id);
                islandLocs.addAll(Arrays.asList(thisIslandLocs));
            }
        }
    }

    /** Gather from squares around the robot. **/
    public static void gatherAdjacentSquares(RobotController rc, MapLocation me) throws GameActionException {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation adjacentLoc = new MapLocation(me.x + dx, me.y + dy);
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
        if (rc.canTakeAnchor(hqLocation, Anchor.STANDARD)) {
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
        if ((amount > 0) && rc.canTransferResource(hqLocation, type, amount)) {
            rc.transferResource(hqLocation, type, amount);
            rc.setIndicatorString("Depositing, now have, AD:" +
                    rc.getResourceAmount(ResourceType.ADAMANTIUM) +
                    " MN: " + rc.getResourceAmount(ResourceType.MANA) +
                    " EX: " + rc.getResourceAmount(ResourceType.ELIXIR));
        }
    }

    /** Move the robot in a given direction. **/
    public static void moveToLocation(RobotController rc, Direction dir) throws GameActionException {
        // If the robot can move, move in that direction. Otherwise, try to move randomly.
        if (rc.canMove(dir)) {
            rc.move(dir);
            rc.setIndicatorString("Moving in direction: " + dir);
        }
        else {
            Direction randDir = directions[rng.nextInt(directions.length)];
            if (rc.canMove(randDir)) {
                rc.move(randDir);
                rc.setIndicatorString("Moving randomly in direction: " + randDir);
            }
        }
    }
}