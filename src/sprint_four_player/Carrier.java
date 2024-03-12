package sprint_four_player;

import battlecode.common.*;

public class Carrier {
    // Map locations to store headquarters, closest well, and island positions.
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
        MapLocation myLocation = rc.getLocation(); // Get robot's current location.

        // Locate the closest HQ.
        MapLocation hqLocation = Communication.readHQ(rc);

        // Record wells and islands.
        Communication.writeWells(rc);
        Communication.writeIslands(rc);

        // Find closest well location.
        MapLocation wellLocation = Communication.readWell(rc, 0);

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
            handleAnchor(rc, myLocation, islandLocation);
        }
        else if (rc.getWeight() < GameConstants.CARRIER_CAPACITY && !isNeededAtHQ) {
            // If there is capacity, then go collect resources.
            handleResourceCollection(rc, myLocation, wellLocation);
        }
        // Head to designated Elixir well to create it or HQ if your resources are full.
        else {
            if (canBuildElixirWell(rc, designatedWellType) && !Communication.isElixirSatisfied(rc) && elixirDepositHistory == 0) {
                // Create Elixir well
                handleElixirCreation(rc, designatedElixirWell, designatedWellType);
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
    public static void handleElixirCreation(RobotController rc, MapLocation wellLoc, int wellType) throws GameActionException {
        Movement.moveToLocation(rc, wellLoc);
        elixirDepositHistory = buildElixirWell(rc, wellLoc, wellType);
    }

    /** Handle resource collection or exploring and collecting anything otherwise **/
    public static void handleResourceCollection(RobotController rc, MapLocation myLocation, MapLocation wellLoc) throws GameActionException {
        if (wellLoc != null) {
            rc.setIndicatorString("Moving towards well at: " + wellLoc);
            Movement.moveToLocation(rc, wellLoc);
            // -1 indicates to collect all.
            collectFromWell(rc, wellLoc, -1);
        }
        else {
            // Try to collect a well anyway, in case there is one, while exploring.
            collectFromAnywhere(rc, myLocation);
            Movement.explore(rc);
        }
    }

    /** Handle planting anchor or exploring otherwise **/
    public static void handleAnchor(RobotController rc, MapLocation myLocation, MapLocation island) throws GameActionException {
        if (island != null) {
            if (conquerIsland(rc, myLocation, island)) {
                islandLocation = null;
            }
        }
        else {
            Movement.explore(rc);
        }
    }

    /** Locate designated Elixir well **/
    public static void locateDesignatedElixirWell(RobotController rc) throws GameActionException {
        int[] wellProperties = Communication.findPotentialElixirWell(rc);
        if (wellProperties != null) {
            // Indices 0 and 1 represent x and y coordinates.
            designatedElixirWell = new MapLocation(wellProperties[0], wellProperties[1]);
            // Index 2 represents the well type.
            designatedWellType = wellProperties[2];
            rc.setIndicatorString("Future Elixir well located.");
        }
    }

    /** Try to conquer an island **/
    public static boolean conquerIsland(RobotController rc, MapLocation myLocation, MapLocation island) throws GameActionException {
        rc.setIndicatorString("Moving my anchor towards " + island);
        if (!myLocation.equals(island)) {
            Movement.moveToLocation(rc, island);
        }
        if (rc.canPlaceAnchor()) {
            rc.placeAnchor();
            collectingAnchor = null;
            rc.setIndicatorString("Huzzah, placed anchor!");
            // Updates if island to be occupied by team.
            Communication.updateIslands(rc, island, 1);
            return true;
        }
        return false;
    }

    /** Deposit opposite resource to create Elixir well **/
    public static int buildElixirWell(RobotController rc, MapLocation wellLocation, int wellType) throws GameActionException {
        boolean deposited = false;
        int amountToDeposit = 0;
        if (wellType == 1) {
            amountToDeposit = rc.getResourceAmount(ResourceType.MANA);
            deposited = depositResource(rc, wellLocation, ResourceType.MANA, amountToDeposit);
        }
        else if (wellType == 2){
            amountToDeposit = rc.getResourceAmount(ResourceType.ADAMANTIUM);
            deposited = depositResource(rc, wellLocation, ResourceType.ADAMANTIUM, amountToDeposit);
        }
        if (deposited) {
            return amountToDeposit;
        }
        return 0;
    }

    /** Check if Carrier can build Elixir **/
    public static boolean canBuildElixirWell(RobotController rc, int wellType) {
        // Returns true if Carrier has opposite resource.
        return (wellType == 1 && rc.getResourceAmount(ResourceType.MANA) > 0)
                || (wellType == 2 && rc.getResourceAmount(ResourceType.ADAMANTIUM) > 0);
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
    public static void collectFromAnywhere(RobotController rc, MapLocation myLocation) throws GameActionException {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation adjacentLoc = new MapLocation(myLocation.x + dx, myLocation.y + dy);
                collectFromWell(rc, adjacentLoc, -1);
            }
        }
    }

    /** Try to collect resource. **/
    public static void collectFromWell(RobotController rc, MapLocation wellLoc, int amount) throws GameActionException {
        if (rc.canCollectResource(wellLoc, amount)) {
            rc.collectResource(wellLoc, amount);
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