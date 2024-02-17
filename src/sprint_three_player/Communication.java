package sprint_three_player;

import battlecode.common.*;
import sprint_three_player.Movement;

import java.util.HashSet;
import java.util.Set;

/**
 * Current array format and indices:
 *      Up to 4 headquarters stored from indices 0 to 3
 *      - x, y coordinate converted into number and bitwise shifted
 *      Up to 10 wells stored from indices 4 to 13
 *      - x, y coordinate converted into number and bitwise shifted left with well type (1 - Adamantium, 2 - Mana, 3 - Elixir)
 *      Up to 27 islands (75% of max) from indices 14 to 40
 *      - uses same format as wells, but instead of containing type, it contains whether it is occupied (1 - yes, 0 - no)
 *      Index 41 is reserved for which resource to prioritize
 **/
public class Communication {
    // Number of headquarters, wells, and islands allowed in communication array.
    private static final int NUM_HQ = GameConstants.MAX_STARTING_HEADQUARTERS;
    private static final int NUM_WELLS = 10;
    private static final int NUM_ISLANDS = 27;

    // Array index increment amounts.
    private static final int XY_IDX_INCREMENT = 2;

    // Starting index for reading and writing from specified location types.
    private static final int START_HQ_IDX = 0;
    private static final int START_WELL_IDX = NUM_HQ;
    private static final int START_ISLAND_IDX = START_WELL_IDX + NUM_WELLS;
    private static final int PRIORITY_IDX = START_ISLAND_IDX + NUM_ISLANDS;

    // Bit shift amount for storage.
    private static final int BIT_SHIFT = 4;

    /** Read headquarter location closest to robot. **/
    public static MapLocation readHQ(RobotController rc) throws GameActionException {
        Set<MapLocation> hqLocations = new HashSet<>();
        // Read all headquarters.
        for (int i = START_HQ_IDX; i < START_WELL_IDX; ++i) {
            int valueToUnpack = rc.readSharedArray(i);
            if (valueToUnpack != 0) {
                hqLocations.add(unpackHQ(rc, valueToUnpack));
            }
        }
        // Return the closest headquarters or return null.
        return Movement.getClosestLocation(rc, hqLocations);
    }

    /**
     * Write headquarter location to array during first round only.
     * Reserved for HQ use only, so no sensing is required.
     * **/
    public static void writeHQ(RobotController rc) throws GameActionException {
        MapLocation hqLoc = rc.getLocation();
        // Write location into first available index.
        for (int i = START_HQ_IDX; i < START_WELL_IDX; i += XY_IDX_INCREMENT) {
            if (rc.readSharedArray(i) == 0 && hqLoc != null && rc.canWriteSharedArray(i, hqLoc.x)) {
                rc.writeSharedArray(i, hqLoc.x);
                rc.writeSharedArray(i + 1, hqLoc.y); // This next index should be guaranteed to be open.
                break;
            }
        }
    }

    /** Read well location closest to robot. **/
    public static MapLocation readWell(RobotController rc) throws GameActionException {
        Set<MapLocation> wellLocations = new HashSet<>();
        // Read all wells.
        for (int i = START_WELL_IDX; i < START_ISLAND_IDX; i += XY_IDX_INCREMENT) {
            if (rc.readSharedArray(i) != 0) {
                wellLocations.add(new MapLocation(rc.readSharedArray(i), rc.readSharedArray(i + 1)));
            }
        }
        // Return the closest well or return null.
        return Movement.getClosestLocation(rc, wellLocations);
    }

    /** Write well location to array. For Carrier and Launcher use. **/
    public static void writeWells(RobotController rc) throws GameActionException {
        WellInfo[] wells = rc.senseNearbyWells();
        // Find all nearby well locations.
        if (wells.length > 0) {
            MapLocation[] wellLocations = new MapLocation[wells.length];
            // For as many wells as possible, add them to array.
            for (int i = 0; i < wellLocations.length; ++i) {
                wellLocations[i] = wells[i].getMapLocation();
                // Write location into first available index.
                for (int j = START_WELL_IDX; j < START_ISLAND_IDX; j += XY_IDX_INCREMENT) {
                    if (rc.readSharedArray(j) == 0) {
                        if (rc.canWriteSharedArray(j, wellLocations[i].x)) {
                            rc.writeSharedArray(j, wellLocations[i].x);
                            rc.writeSharedArray(j + 1, wellLocations[i].y); // Guaranteed to be open.
                            break;
                        }
                    }
                    else if (rc.readSharedArray(j) == wellLocations[i].x && rc.readSharedArray(j + 1) == wellLocations[i].y) {
                        // If well already exists in array, break out of loop.
                        break;
                    }
                }
            }
        }
    }

    /** Read island location closest to robot. **/
    public static MapLocation readIsland(RobotController rc) throws GameActionException {
        Set<MapLocation> islandLocations = new HashSet<>();
        // Read all islands.
        for (int i = START_ISLAND_IDX; i < PRIORITY_IDX; i += XY_IDX_INCREMENT) {
            if (rc.readSharedArray(i) != 0) {
                islandLocations.add(new MapLocation(rc.readSharedArray(i), rc.readSharedArray(i + 1)));
            }
        }
        // Return the closest well or return null.
        return Movement.getClosestLocation(rc, islandLocations);
    }

    /** Write island location to array. For Carrier and Launcher use. **/
    public static void writeIslands(RobotController rc) throws GameActionException {
        int[] islands = rc.senseNearbyIslands();
        Set<MapLocation> closestIslands = new HashSet<>();
        for (int id : islands) {
            // Check if island is unoccupied first.
            if (rc.senseTeamOccupyingIsland(id) == Team.NEUTRAL) {
                MapLocation[] thisIslandLocs = rc.senseNearbyIslandLocations(id);
                // Add the closest parts of each island.
                if (thisIslandLocs.length > 0) {
                    closestIslands.add(Movement.getClosestLocation(rc, thisIslandLocs));
                }
            }
        }
        // Add as many islands as possible.
        if (!closestIslands.isEmpty()) {
            for (MapLocation island : closestIslands) {
                // Write location into first available index.
                for (int i = START_ISLAND_IDX; i < PRIORITY_IDX; i += XY_IDX_INCREMENT) {
                    if (rc.readSharedArray(i) == 0) {
                        if (rc.canWriteSharedArray(i, island.x)) {
                            rc.writeSharedArray(i, island.x);
                            rc.writeSharedArray(i + 1, island.y); // Guaranteed to be open.
                            break;
                        }
                    }
                    else if (rc.readSharedArray(i) == island.x && rc.readSharedArray(i + 1) == island.y) {
                        // If island already exists, skip this iteration.
                        break;
                    }
                }
            }
        }
    }

    /** Update array of islands, removing any that are now occupied. **/
    public static void updateIslands(RobotController rc, MapLocation island) throws GameActionException {
        // Island is in array, remove it.
        for (int i = START_ISLAND_IDX; i < PRIORITY_IDX; i += XY_IDX_INCREMENT) {
            if (rc.readSharedArray(i) == island.x && rc.readSharedArray(i + 1) == island.y) {
                if (rc.canWriteSharedArray(i, 0)) {
                    rc.writeSharedArray(i, 0);
                    rc.writeSharedArray(i + 1, 0);
                }
            }
        }
    }


    /** Read which resource to prioritize. **/
    public static int readPriority(RobotController rc) throws GameActionException {
        return rc.readSharedArray(PRIORITY_IDX);
    }

    /** Write which resource to prioritize. For Headquarters use. **/
    public static void writePriority(RobotController rc, int priorityType) throws GameActionException {
        if (rc.canWriteSharedArray(PRIORITY_IDX, priorityType)) {
            rc.writeSharedArray(PRIORITY_IDX, priorityType);
        }
    }

    /** Pack coordinates into one representative value **/
    public static int packCoordinates(RobotController rc, MapLocation location) throws GameActionException {
        return 1 + location.x + location.y * rc.getMapWidth();
    }

    /** Unpack coordinates from one representative value **/
    public static MapLocation unpackCoordinates(RobotController rc, int mapValue) throws GameActionException {
        int x = (mapValue - 1) % rc.getMapWidth();
        int y = (mapValue - 1) / rc.getMapWidth();
        return new MapLocation(x, y);
    }

    /** Pack HQ information into one representative value **/
    public static int packHQ(RobotController rc, MapLocation hqLocation) throws GameActionException {
        int packedCoordinates = packCoordinates(rc, hqLocation);
        return (packedCoordinates & 0xFFF) << BIT_SHIFT;
    }

    /** Unpack HQ information from one representative value **/
    public static MapLocation unpackHQ(RobotController rc, int valueToUnpack) throws GameActionException {
        int mapValue = (valueToUnpack >> BIT_SHIFT) & 0xFFF;
        return unpackCoordinates(rc, mapValue);
    }
}