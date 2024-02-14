package sprint_three_player;

import battlecode.common.*;
import sprint_three_player.Movement;

import java.util.HashSet;
import java.util.Set;

/**
 * Current array format:
 *      Headquarters: store x-y coordinate locations in x-y coordinate index pairings.
 *      Wells: store x-y coordinate locations in x-y coordinate index pairings.
 *      Islands: store x-y coordinate locations in x-y coordinate index pairings.
 *      Resource Priority: store 1 for Adamantium or 2 for Mana (3 will be for Elixir in future sprint).
 *      Behavioral State: to be determined (will store a different number representing a different state to be in).
 * Current array indices:
 *      Up to 4 headquarters stored from indices 0 to 7
 *      - (i.e., index 0 and 1 represent x and y coordinate of a Headquarters, respectively)
 *      Up to 12 wells stored from indices 8 to 31
 *      - (uses same x-y pairing method as with headquarters)
 *      Up to 15 islands stored from indices 32 to 61
 *      - (uses same x-y pairing method as with headquarters)
 *      Index 62 is reserved for which resource to prioritize
 *      Index 63 is reserved for behavioral state (reserved for next sprint possibly)
 **/
public class Communication {
    // Number of headquarters, wells, and islands allowed in communication array.
    private static final int NUM_HQ = GameConstants.MAX_STARTING_HEADQUARTERS;
    private static final int NUM_WELLS = 12;
    private static final int NUM_ISLANDS = 15;

    // Array index increment amounts.
    private static final int XY_IDX_INCREMENT = 2;

    // Starting index for reading and writing from specified location types.
    private static final int START_HQ_IDX = 0;
    private static final int START_WELL_IDX = NUM_HQ * XY_IDX_INCREMENT;
    private static final int START_ISLAND_IDX = START_WELL_IDX + (NUM_WELLS * XY_IDX_INCREMENT);
    private static final int PRIORITY_IDX = START_ISLAND_IDX + (NUM_ISLANDS * XY_IDX_INCREMENT);
//    private static final int STATE_IDX = PRIORITY_IDX + 1;

    /** Read headquarter location closest to robot. **/
    public static MapLocation readHQ(RobotController rc) throws GameActionException {
        Set<MapLocation> hqLocations = new HashSet<>();
        // Read all headquarters.
        for (int i = START_HQ_IDX; i < START_WELL_IDX; i += XY_IDX_INCREMENT) {
            if (rc.readSharedArray(i) != 0) {
                hqLocations.add(new MapLocation(rc.readSharedArray(i), rc.readSharedArray(i + 1)));
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

//     For behavior state usage in possible future sprint.
//    /** Read what behavior state we are in. **/
//    public static int readState(RobotController rc) throws GameActionException {
//        return rc.readSharedArray(STATE_IDX);
//    }
//
//    /** Write what behavior state to be in. **/
//    public static void writeState(RobotController rc, int stateType) throws GameActionException {
//        if (rc.canWriteSharedArray(STATE_IDX, stateType)) {
//            rc.writeSharedArray(STATE_IDX, stateType);
//        }
//    }
}