package sprint_four_player;

import battlecode.common.*;

import java.awt.*;

public class Carrier {
    // Map locations to store headquarters, closest well, and island positions.
    private static MapLocation myLocation;
    private static MapLocation wellLocation;
    private static MapLocation designatedElixirWell;
    private static int designatedWellType;
    private static MapLocation islandLocation;
    private static Anchor collectingAnchor;
    private static boolean isNeededAtHQ = false;
    private static int elixirDepositHistory;

    /**
     * Run a single turn for a Carrier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runCarrier(RobotController rc) throws GameActionException {
        myLocation = rc.getLocation(); // Get robot's current location.

        // Locate the closest HQ.
        MapLocation hqLocation = Communication.readHQ(rc);

        // Record wells and islands.
        Communication.writeWells(rc);
        Communication.writeIslands(rc);

        // Find closest well location.
        wellLocation = Communication.readWell(rc, 0);

        if (designatedElixirWell == null) {
            // If no designated Elixir well, find one.
            locateDesignatedElixirWell(rc);
        }

        if (islandLocation == null) {
            // If the closest unoccupied island has not been found, locate it.
            islandLocation = Communication.readIsland(rc, 0);
        }

        if (collectingAnchor == null) {
            // If the robot does not have an anchor, try to collect one.
            collectAnchor(rc, hqLocation);
        }

        if (collectingAnchor != null) {
            // If the robot has an anchor singularly focus on getting it to the closest island it sees.
            handleAnchor(rc);
        }
        else if (rc.getWeight() < GameConstants.CARRIER_CAPACITY && !isNeededAtHQ) {
            // If there is capacity, then go collect resources.
            handleResourceCollection(rc);
        }
        // Head to designated Elixir well to create it or HQ if your resources are full.
        else {
            if (canBuildElixirWell(rc) && !Communication.isElixirSatisfied(rc) && elixirDepositHistory == 0) {
                // Create Elixir well
                handleElixirCreation(rc);
            }
            else {
                // Deposit resources and update whether resources deposited to Elixir well.
                handleHQOperations(rc, hqLocation);
            }
        }
    }

    /** Handle Headquarters operations, depositing and writing to array. **/
    public static void handleHQOperations(RobotController rc, MapLocation hqLocation) throws GameActionException {
        isNeededAtHQ = true;
        Movement.moveToLocation(rc, hqLocation);
        // Deposit resources.
        depositResource(rc, hqLocation, ResourceType.ADAMANTIUM, rc.getResourceAmount(ResourceType.ADAMANTIUM));
        depositResource(rc, hqLocation, ResourceType.MANA, rc.getResourceAmount(ResourceType.MANA));
        depositResource(rc, hqLocation, ResourceType.ELIXIR, rc.getResourceAmount(ResourceType.ELIXIR));

        if (rc.getWeight() == 0) {
            isNeededAtHQ = false;
            if (Communication.updateElixirAmount(rc, elixirDepositHistory)) {
                elixirDepositHistory = 0;
            }
        }
    }

    /** Handle creation of an Elixir well **/
    public static void handleElixirCreation(RobotController rc) throws GameActionException {
        Movement.moveToLocation(rc, designatedElixirWell);
        elixirDepositHistory = buildElixirWell(rc);
    }

    /** Handle resource collection or exploring and collecting anything otherwise **/
    public static void handleResourceCollection(RobotController rc) throws GameActionException {
        if (wellLocation != null) {
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

    /** Handle planting anchor or exploring otherwise **/
    public static void handleAnchor(RobotController rc) throws GameActionException {
        if (islandLocation != null) {
            conquerIsland(rc);
        }
        else {
            Movement.explore(rc);
        }
    }

    /** Locate designated Elixir well **/
    public static void locateDesignatedElixirWell(RobotController rc) throws GameActionException {
        int[] wellProperties = Communication.findFirstWell(rc);
        if (wellProperties != null) {
            // Indices 0 and 1 represent x and y coordinates.
            designatedElixirWell = new MapLocation(wellProperties[0], wellProperties[1]);
            // Index 2 represents the well type.
            designatedWellType = wellProperties[2];
        }
    }

    /** Try to conquer an island **/
    public static void conquerIsland(RobotController rc) throws GameActionException {
        rc.setIndicatorString("Moving my anchor towards " + islandLocation);
        if (!myLocation.equals(islandLocation)) {
            Movement.moveToLocation(rc, islandLocation);
        }
        if (rc.canPlaceAnchor()) {
            rc.placeAnchor();
            collectingAnchor = null;
            rc.setIndicatorString("Huzzah, placed anchor!");
            // Updates if island to be occupied by team.
            Communication.updateIslands(rc, islandLocation, 1);
            islandLocation = null;
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

    /** Deposit opposite resource to create Elixir well **/
    public static int buildElixirWell(RobotController rc) throws GameActionException {
        boolean deposited;
        int amountToDeposit;
        if (designatedWellType == 1) {
            amountToDeposit = rc.getResourceAmount(ResourceType.MANA);
            deposited = depositResource(rc, designatedElixirWell, ResourceType.MANA, amountToDeposit);
        }
        else {
            amountToDeposit = rc.getResourceAmount(ResourceType.ADAMANTIUM);
            deposited = depositResource(rc, designatedElixirWell, ResourceType.ADAMANTIUM, amountToDeposit);
        }
        if (deposited) {
            return amountToDeposit;
        }
        return 0;
    }

    /** Check if Carrier can build Elixir **/
    public static boolean canBuildElixirWell(RobotController rc) throws GameActionException {
        // Returns true if Carrier has opposite resource.
        return (designatedWellType == 1 && rc.getResourceAmount(ResourceType.MANA) > 0)
                || (designatedWellType == 2 && rc.getResourceAmount(ResourceType.ADAMANTIUM) > 0);
    }

    /** Deposit all resources of a certain type to headquarters. **/
    public static boolean depositResource(RobotController rc, MapLocation location, ResourceType type, int amount) throws GameActionException {
        // If robot has any resources, deposit all of it.
        if (amount > 0 && rc.canTransferResource(location, type, amount)) {
            rc.transferResource(location, type, amount);
            rc.setIndicatorString("Depositing, now have, AD:" +
                    rc.getResourceAmount(ResourceType.ADAMANTIUM) +
                    " MN: " + rc.getResourceAmount(ResourceType.MANA) +
                    " EX: " + rc.getResourceAmount(ResourceType.ELIXIR));
            return true;
        }
        return false;
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
        }
        else if (rc.canTakeAnchor(location, Anchor.ACCELERATING)) {
            rc.takeAnchor(location, Anchor.ACCELERATING);
        }
        collectingAnchor = rc.getAnchor();

        if (collectingAnchor != null) {
            rc.setIndicatorString("Taking anchor, now have, Anchor: " + collectingAnchor);
        }
    }
}