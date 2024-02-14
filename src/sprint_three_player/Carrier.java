package sprint_three_player;

import battlecode.common.*;
import sprint_three_player.Communication;
import sprint_three_player.Movement;

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
        myLocation = rc.getLocation();                                  // Get robot's current location.

        // If the headquarters have not already been found, locate the closest one.
        if (hqLocation == null) {
            hqLocation = Communication.readHQ(rc);
        }

        // If the closest well has not been found, locate it.
        Communication.writeWells(rc);
        if (wellLocation == null) {
            wellLocation = Communication.readWell(rc);
        }

        // If the closest island has not been found, locate it.
        Communication.writeIslands(rc);
        if (islandLocation == null) {
            islandLocation = Communication.readIsland(rc);
        }

        // If the robot does not have an anchor, try to collect one.
        if (rc.getAnchor() == null) {
            collectAnchor(rc);
        }

        // If the robot has an anchor singularly focus on getting it to the first island it sees.
        if (rc.getAnchor() != null || rc.getAnchor() == Anchor.STANDARD || rc.getAnchor() == Anchor.ACCELERATING) {
            if (islandLocation != null) {
                rc.setIndicatorString("Moving my anchor towards " + islandLocation);
                if (!myLocation.equals(islandLocation)) {
                    Movement.moveToLocation(rc, islandLocation);
                    myLocation = rc.getLocation();
                }
                if (rc.canPlaceAnchor()) {
                    rc.placeAnchor();
                    rc.setIndicatorString("Huzzah, placed anchor!");
                    // Removes island from shared array.
                    Communication.updateIslands(rc, islandLocation);
                    islandLocation = null;
                }
            }
        }
        // If there is capacity, then go collect resources.
        else if (rc.getWeight() < GameConstants.CARRIER_CAPACITY) {
            if (wellLocation != null) {
                rc.setIndicatorString("Moving towards well at: " + wellLocation);
                Movement.moveToLocation(rc, wellLocation);
                myLocation = rc.getLocation();
                // Try to collect whether a well was located or not, in case robot has moved since located.
                collectFromWell(rc);
            }
            else {
                // If no well located, just make sure to head away from HQ.
                if (hqLocation != null) {
                    Direction awayFromHQ = myLocation.directionTo(hqLocation).opposite();
                    Movement.moveToLocation(rc, awayFromHQ);
                    myLocation = rc.getLocation();
                }
            }
        }
        // Head to HQ if your resources are full.
        else if (hqLocation != null) {
            wellLocation = null; // Reset well location if full of resources.
            if (!myLocation.isAdjacentTo(hqLocation)) {
                Movement.moveToLocation(rc, hqLocation);
                myLocation = rc.getLocation();
            }
            // Deposit resources and try to collect an anchor immediately.
            if (myLocation.isAdjacentTo(hqLocation)) {
                // If next to headquarters, deposit everything from the carrier.
                depositResource(rc, ResourceType.MANA);
                depositResource(rc, ResourceType.ADAMANTIUM);
                collectAnchor(rc);
                Movement.moveToLocation(rc, myLocation.directionTo(hqLocation).opposite());
            }
        }
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