package sprint_one_player;

import battlecode.common.*;

import java.util.Arrays;
import java.util.HashSet;
//import java.util.Random;
import java.util.Set;

import static sprint_one_player.RobotPlayer.directions;
import static sprint_one_player.RobotPlayer.rng;

public class Carrier {
    // Map locations to store headquarters and well position.
    private static MapLocation hqLocation;
    private static MapLocation wellLocation;

    /**
     * Run a single turn for a Carrier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runCarrier(RobotController rc) throws GameActionException {
        // Get robot's current location.
        MapLocation me = rc.getLocation();

        // If the headquarters have not already been found, locate it.
        if (hqLocation == null)
            locateHQ(rc);

        // If the robot does not have an anchor, try to collect one.
        if (rc.getAnchor() == null) {
            collectAnchor(rc);
        }

        if (rc.getAnchor() != null) {
            // If I have an anchor singularly focus on getting it to the first island I see
            int[] islands = rc.senseNearbyIslands();
            Set<MapLocation> islandLocs = new HashSet<>();
            for (int id : islands) {
                MapLocation[] thisIslandLocs = rc.senseNearbyIslandLocations(id);
                islandLocs.addAll(Arrays.asList(thisIslandLocs));
            }
            if (!islandLocs.isEmpty()) {
                MapLocation islandLocation = islandLocs.iterator().next();
                rc.setIndicatorString("Moving my anchor towards " + islandLocation);
                while (!rc.getLocation().equals(islandLocation)) {
                    Direction dir = me.directionTo(islandLocation);
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                        me = rc.getLocation();
                    }
                }
                if (rc.canPlaceAnchor()) {
                    rc.setIndicatorString("Huzzah, placed anchor!");
                    rc.placeAnchor();
                }
            }
        }
        // Try to gather from squares around us.
        gatherAdjacentSquares(rc, me);

        // If next to headquarters, deposit everything from the carrier.
        depositResource(rc, ResourceType.MANA);
        depositResource(rc, ResourceType.ADAMANTIUM);

        // Occasionally try out the carriers attack
//        if (rng.nextInt(20) == 1) {
//            RobotInfo[] enemyRobots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
//            if (enemyRobots.length > 0) {
//                if (rc.canAttack(enemyRobots[0].location)) {
//                    rc.attack(enemyRobots[0].location);
//                }
//            }
//        }

        // If the robot has capacity, move toward a nearby well.
        if (rc.getWeight() < GameConstants.CARRIER_CAPACITY) {
            locateWell(rc, me);
            if (wellLocation != null) {
                Direction dir = me.directionTo(wellLocation);
                if (rc.canMove(dir))
                    rc.move(dir);
            }
        }
        // If the robot's capacity is full, move toward headquarters.
        else if (hqLocation != null) {
            Direction dir = me.directionTo(hqLocation);
            if (rc.canMove(dir))
                rc.move(dir);
        } else {
            // Also try to move randomly.
            Direction dir = directions[rng.nextInt(directions.length)];
            if (rc.canMove(dir)) {
                rc.move(dir);
            }
        }
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

        // Find the closest well if more than one is nearby.
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
            // Move toward the closest well.
            wellLocation = closestWell.getMapLocation();
            rc.setIndicatorString("Found closest well at: " + wellLocation);
        }
        // If only one well is nearby, move to it.
        else if (wells.length == 1) {
            wellLocation = wells[0].getMapLocation();
            rc.setIndicatorString("Found well at: " + wellLocation);
        }
        else {
            rc.setIndicatorString("No wells are nearby.");
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
        if ((amount > 0) && rc.canTransferResource(hqLocation, type, amount))
            rc.transferResource(hqLocation, type, amount);
        rc.setIndicatorString("Depositing, now have, AD:" +
                rc.getResourceAmount(ResourceType.ADAMANTIUM) +
                " MN: " + rc.getResourceAmount(ResourceType.MANA) +
                " EX: " + rc.getResourceAmount(ResourceType.ELIXIR));
    }

}
