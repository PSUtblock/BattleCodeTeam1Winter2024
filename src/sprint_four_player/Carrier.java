package sprint_four_player;

import battlecode.common.*;

public class Carrier {
    // Map locations to store headquarters, closest well, and island positions.
    private static MapLocation myLocation;
    private static MapLocation wellLocation;
    private static Anchor hasAnchorType = null;

    /**
     * Run a single turn for a Carrier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runCarrier(RobotController rc) throws GameActionException {
        myLocation = rc.getLocation();        // Get robot's current location.
        int roundNum = rc.getRoundNum();

        // Locate the closest HQ.
        MapLocation hqLocation = Communication.readHQ(rc);

        // Record wells and islands.
        Communication.writeWells(rc);
        Communication.writeIslands(rc);

        // Depending on round number, collect specific resource type.
        if (wellLocation == null) {
            wellLocation = getStandardWell(rc, roundNum);
        }

        // If the closest unoccupied island has not been found, locate it.
        MapLocation islandLocation = Communication.readIsland(rc, 0);

        // If the robot does not have an anchor, try to collect one.
        if (hasAnchorType == null) {
            collectAnchor(rc, hqLocation);
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
                    hasAnchorType = null;
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
                depositResource(rc, hqLocation, ResourceType.ADAMANTIUM, rc.getResourceAmount(ResourceType.ADAMANTIUM));
                depositResource(rc, hqLocation, ResourceType.MANA, rc.getResourceAmount(ResourceType.MANA));

                rc.setIndicatorString("Moving towards well at: " + wellLocation);
                Movement.moveToLocation(rc, wellLocation);
                // -1 indicates to collect all.
                collectFromWell(rc, wellLocation, -1);
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
            // Deposit resources.
            depositResource(rc, hqLocation, ResourceType.ADAMANTIUM, rc.getResourceAmount(ResourceType.ADAMANTIUM));
            depositResource(rc, hqLocation, ResourceType.MANA, rc.getResourceAmount(ResourceType.MANA));
            wellLocation = null;
        }
    }

    /** Sense number of carriers at well location **/
    public static int numCarriersAtWell(RobotController rc) throws GameActionException {
        RobotInfo[] nearbyCarriers = Mapping.getNearbyAllyCarriers(rc);
        int carrierCount = 0;
        for (RobotInfo carrierRobot : nearbyCarriers) {
            MapLocation carrierLoc = carrierRobot.getLocation();
            if (carrierLoc.isWithinDistanceSquared(wellLocation, rc.getType().actionRadiusSquared)) {
                ++carrierCount;
            }
        }
        return carrierCount;
    }

    /** Get specific standard well based on round number **/
    public static MapLocation getStandardWell(RobotController rc, int roundNum) throws GameActionException {
        if (roundNum % 2 == 0) {
            // Collect Adamantium on even rounds.
            return Communication.readWell(rc, 1);
        }
        // Collect Mana on odd rounds.
        return Communication.readWell(rc, 2);
    }

    /** Deposit all resources of a certain type to headquarters. **/
    public static void depositResource(RobotController rc, MapLocation location, ResourceType type, int amount) throws GameActionException {
        // If robot has any resources, deposit all of it.
        if ((amount > 0) && rc.canTransferResource(location, type, amount)) {
            rc.transferResource(location, type, amount);
            rc.setIndicatorString("Depositing, now have, AD:" +
                    rc.getResourceAmount(ResourceType.ADAMANTIUM) +
                    " MN: " + rc.getResourceAmount(ResourceType.MANA) +
                    " EX: " + rc.getResourceAmount(ResourceType.ELIXIR));
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
    public static void collectAnchor(RobotController rc, MapLocation location) throws GameActionException {
        if (rc.canTakeAnchor(location, Anchor.STANDARD)) {
            rc.takeAnchor(location, Anchor.STANDARD);
            hasAnchorType = rc.getAnchor();
            rc.setIndicatorString("Taking anchor, now have, Anchor: " + rc.getAnchor());
        }
        else {
            rc.setIndicatorString("Unable to collect anchor.");
        }
    }
}