package sprint_two_player;

import battlecode.common.*;

import java.util.HashSet;
import java.util.Set;

import static sprint_two_player.RobotPlayer.directions;
import static sprint_two_player.RobotPlayer.rng;

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

        // If the headquarters have not already been found, locate the closest one.
        if (hqLocation == null) {
            hqLocation = Communication.readHQ(rc);
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
        /**
         * While debugging, if the robot has an anchor, the condition still return true that it is null,
         * so I've added more conditions to ensure it returns the correct result.
         */
        if (rc.getAnchor() == null && rc.getAnchor() != Anchor.STANDARD && rc.getAnchor() != Anchor.ACCELERATING) {
            collectAnchor(rc);
        }

        // If the robot has an anchor singularly focus on getting it to the first island it sees.
        /**
         * While debugging, if the robot has an anchor, the condition returns false,
         * so I've added more conditions to ensure it returns the correct result.
         */
        if (rc.getAnchor() != null || rc.getAnchor() == Anchor.STANDARD || rc.getAnchor() == Anchor.ACCELERATING) {
            if (islandLocation != null) {
                rc.setIndicatorString("Moving my anchor towards " + islandLocation);
                if (!myLocation.equals(islandLocation)) {
                    Movement.moveToLocation(rc, myLocation.directionTo(islandLocation));
                    myLocation = rc.getLocation();
                }
                if (rc.canPlaceAnchor()) {
                    rc.placeAnchor();
                    rc.setIndicatorString("Huzzah, placed anchor!");
                    islandLocation = null;
                }
            }
            else {
                Movement.moveToLocation(rc, randDir);
                myLocation = rc.getLocation();
            }
        }
        // If there is capacity, then go collect resources.
        else if (rc.getWeight() < GameConstants.CARRIER_CAPACITY) {
            if (wellLocation != null) {
                rc.setIndicatorString("Moving towards well at: " + wellLocation);
                Movement.moveToLocation(rc, myLocation.directionTo(wellLocation));
                myLocation = rc.getLocation();
            }
            // Try to collect whether a well was located or not, in case robot has moved since located.
            collectFromWell(rc);
        }
        // Head to HQ if your resources are full.
        else if (hqLocation != null) {
            wellLocation = null; // Reset well location if full of resources.
            if (!myLocation.isAdjacentTo(hqLocation)) {
                Movement.moveToLocation(rc, myLocation.directionTo(hqLocation));
                myLocation = rc.getLocation();
            }
            // If next to headquarters, deposit everything from the carrier.
            depositResource(rc, ResourceType.MANA);
            depositResource(rc, ResourceType.ADAMANTIUM);
        }
        // Otherwise, move randomly for now.
        else {
            Movement.moveToLocation(rc, randDir);
            myLocation = rc.getLocation();
        }

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
        Communication.writeWells(rc);
        wellLocation = Communication.readWell(rc);
        rc.setIndicatorString("Found closest well at: " + wellLocation);
    }

    /** Locate closest unoccupied island nearby. **/
    public static void locateIsland(RobotController rc) throws GameActionException {
        Communication.writeIslands(rc);
        islandLocation = Communication.readIsland(rc);
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
}