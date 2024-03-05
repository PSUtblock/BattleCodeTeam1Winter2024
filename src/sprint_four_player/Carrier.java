package sprint_four_player;

import battlecode.common.*;

public class Carrier {
    // Map locations to store headquarters, closest well, and island positions.
    private static MapLocation hqLocation;
    private static MapLocation myLocation;
    private static Anchor hasAnchorType = null;

    /**
     * Run a single turn for a Carrier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runCarrier(RobotController rc) throws GameActionException {
        myLocation = rc.getLocation();        // Get robot's current location.

        // If the headquarters have not already been found, locate the closest one.
        if (hqLocation == null) {
            hqLocation = Communication.readHQ(rc);
        }

        // If the closest well has not been found, locate it.
        Communication.writeWells(rc);
        MapLocation wellLocation = Communication.readWell(rc, 0);

        // If the closest unoccupied island has not been found, locate it.
        Communication.writeIslands(rc);
        MapLocation islandLocation = Communication.readIsland(rc, 0);

        // If the robot does not have an anchor, try to collect one.
        if (hasAnchorType == null) {
            collectAnchor(rc);
        }

        // If the robot has an anchor singularly focus on getting it to the first island it sees.
        if (hasAnchorType != null) {
            if (islandLocation != null) {
                rc.setIndicatorString("Moving my anchor towards " + islandLocation);
                if (!myLocation.equals(islandLocation)) {
                    Movement.moveToLocation(rc, islandLocation);
                }
                if (rc.canPlaceAnchor()) {
                    rc.placeAnchor();
                    rc.setIndicatorString("Huzzah, placed anchor!");
                    // Updates if island to be occupied by team.
                    Communication.updateIslands(rc, islandLocation, 1);
                }
            }
            else {
                Movement.explore(rc);
            }
        }
        // If there is capacity, then go collect resources.
        else if (rc.getWeight() < GameConstants.CARRIER_CAPACITY) {
            if (wellLocation != null) {
                // First check if still next to HQ and deposit anything.
                depositAtHQ(rc);

                rc.setIndicatorString("Moving towards well at: " + wellLocation);
                Movement.moveToLocation(rc, wellLocation);
                myLocation = rc.getLocation();
                if (myLocation.isAdjacentTo(wellLocation) || myLocation.equals(wellLocation)) {
                    // -1 indicates to collect all.
                    collectFromWell(rc, wellLocation, -1);
                }
            }
            else {
                // Try to collect a well anyway, in case there is one, while exploring.
                collectFromAnywhere(rc);
                Movement.explore(rc);
            }
        }
        // Head to HQ if your resources are full.
        else if (hqLocation != null) {
            Movement.moveToLocation(rc, hqLocation);
            // Deposit resources and try to collect an anchor immediately.
            depositAtHQ(rc);
        }
    }

    /** Deposit everything at HQ **/
    public static void depositAtHQ(RobotController rc) throws GameActionException {
        if (myLocation.isAdjacentTo(hqLocation)) {
            // If next to headquarters, deposit everything from the carrier.
            depositResource(rc, ResourceType.MANA);
            depositResource(rc, ResourceType.ADAMANTIUM);
        }
    }

    /** Collect from adjacent squares. **/
    public static void collectFromAnywhere(RobotController rc) throws GameActionException {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation adjacentLoc = new MapLocation(myLocation.x + dx, myLocation.y + dy);
                collectFromWell(rc, adjacentLoc, -1);
            }
        }
    }

    /** Try to collect resource. **/
    public static void collectFromWell(RobotController rc, MapLocation location, int amount) throws GameActionException {
        if (rc.canCollectResource(location, amount)) {
            rc.collectResource(location, amount);
            rc.setIndicatorString("Collecting, now have, AD:" +
                    rc.getResourceAmount(ResourceType.ADAMANTIUM) +
                    " MN: " + rc.getResourceAmount(ResourceType.MANA) +
                    " EX: " + rc.getResourceAmount(ResourceType.ELIXIR));
        }
    }

    /** Collect an anchor from headquarters. **/
    public static void collectAnchor(RobotController rc) throws GameActionException {
        if (rc.canTakeAnchor(hqLocation, Anchor.STANDARD)) {
            rc.takeAnchor(hqLocation, Anchor.STANDARD);
            hasAnchorType = rc.getAnchor();
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
}