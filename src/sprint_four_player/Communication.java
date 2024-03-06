package sprint_four_player;

import battlecode.common.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Current array format and indices:
 *      Up to 4 headquarters stored from indices 0 to 3
 *      - x, y coordinate converted into number and bitwise shifted
 *      Up to 12 wells stored from indices 4 to 15
 *      - x, y coordinate converted into number and bitwise shifted left with well type (1 - Adamantium, 2 - Mana, 3 - Elixir)
 *      Up to 30 islands (>75% of max) from indices 16 to 45
 *      - uses same format as wells, but instead of containing type, it contains whether it is occupied (0 - No, 1 - Yes/Team, 2 - Yes/Opponent)
 *      Up to 17 carriers with anchors from indices 46 to 62
 *      - uses same format as headquarters
 *      Index 63 is reserved for which resource to prioritize
 **/
public class Communication {
    // Number of headquarters, wells, and islands allowed in communication array.
    private static final int NUM_HQ = GameConstants.MAX_STARTING_HEADQUARTERS;
    private static final int NUM_WELLS = 12;
    private static final int NUM_ISLANDS = 30;
    private static final int NUM_CARRIERS = 17;

    // Starting index for reading and writing from specified location types.
    private static final int START_HQ_IDX = 0;
    private static final int START_WELL_IDX = NUM_HQ;
    private static final int START_ISLAND_IDX = START_WELL_IDX + NUM_WELLS;
    private static final int START_CARRIER_IDX = START_ISLAND_IDX + NUM_ISLANDS;
    private static final int PRIORITY_IDX = START_CARRIER_IDX + NUM_CARRIERS;

    // Counters for objects in shared array.
//    private static int numOfManaWells = 0;
//    private static int numOfAdamantiumWells = 0;
//    private static int manaWellCount = 0;
//    private static int adamantiumWellCount = 0;
//    private static int numOfIslands = 0;
//    private static int islandCount = 0;

    /** Read headquarter location closest to robot. **/
    public static MapLocation readHQ(RobotController rc) throws GameActionException {
        Set<MapLocation> hqLocations = new HashSet<>();
        // Read all headquarters.
        for (int i = START_HQ_IDX; i < START_WELL_IDX; ++i) {
            int valueToUnpack = rc.readSharedArray(i);
            if (valueToUnpack != 0) {
                // Indices 0 and 1 of unpacked value are x and y values.
                int[] unpackedValue = Packing.unpackObject(rc, valueToUnpack);
                MapLocation locToAdd = new MapLocation(unpackedValue[0], unpackedValue[1]);
                hqLocations.add(locToAdd);
            }
        }
        // Return the closest headquarters or return null.
        return Mapping.getClosestLocation(rc, hqLocations);
    }

    /**
     * Write headquarter location to array during first round only.
     * Reserved for HQ use only, so no sensing is required.
     * **/
    public static void writeHQ(RobotController rc) throws GameActionException {
        MapLocation hqLoc = rc.getLocation();
        if (hqLoc != null) {
            int packedValue = Packing.packObject(rc, hqLoc);
            // Write location into first available index.
            for (int i = START_HQ_IDX; i < START_WELL_IDX; ++i) {
                if (rc.readSharedArray(i) == 0 && rc.canWriteSharedArray(i, packedValue)) {
                    rc.writeSharedArray(i, packedValue);
                    break;
                }
            }
        }
    }

    /** Read well location of a certain type. **/
    public static MapLocation readWell(RobotController rc, int type) throws GameActionException {
        Set<MapLocation> wellLocationsMatch = new HashSet<>();
        Set<MapLocation> wellLocationsNoMatch = new HashSet<>();

        // Read all wells.
        for (int i = START_WELL_IDX; i < START_ISLAND_IDX; ++i) {
            int valueToUnpack = rc.readSharedArray(i);
            if (valueToUnpack != 0) {
                // Indices 0 and 1 of unpackedValue are x and y values.
                int[] unpackedValue = Packing.unpackObject(rc, valueToUnpack);
                MapLocation locToAdd = new MapLocation(unpackedValue[0], unpackedValue[1]);
                int typeValue = unpackedValue[2];
                // Break out of loop if type of object is found.
                if (typeValue == type) {
                    wellLocationsMatch.add(locToAdd);
                }
                else {
                    wellLocationsNoMatch.add(locToAdd);
                }
            }
        }
        // Return the closest well or return null.
        if (!wellLocationsMatch.isEmpty()) {
            return Mapping.getClosestLocation(rc, wellLocationsMatch);
        }
        if (!wellLocationsNoMatch.isEmpty()) {
            return Mapping.getClosestLocation(rc, wellLocationsNoMatch);
        }
        return null;
    }

    /** Write well location to array. For Carrier and Launcher use. **/
    public static void writeWells(RobotController rc) throws GameActionException {
        WellInfo[] wells = rc.senseNearbyWells();
        // Find all nearby well locations.
        if (wells.length > 0) {
            int[] wellSpecs = new int[wells.length];
            // For as many objects as possible, add them to array.
            for (int i = 0; i < wellSpecs.length; ++i) {
                int type = Packing.getWellTypeNum(wells[i].getResourceType());
                wellSpecs[i] = Packing.packObject(rc, wells[i].getMapLocation(), type);
                // Write location into first available index.
                for (int j = START_WELL_IDX; j < START_ISLAND_IDX; ++j) {
                    if (rc.readSharedArray(j) == 0) {
                        if (rc.canWriteSharedArray(j, wellSpecs[i])) {
                            rc.writeSharedArray(j, wellSpecs[i]);
//                            if (type == 1) {
//                                ++numOfAdamantiumWells;
//                            }
//                            else if (type == 2) {
//                                ++numOfManaWells;
//                            }
                            break;
                        }
                    }
                    else if (rc.readSharedArray(j) == wellSpecs[i]) {
                        // If well already exists in array, break out of loop.
                        break;
                    }
                }
            }
        }
    }

    /** Read island location closest to robot. **/
    public static MapLocation readIsland(RobotController rc, int type) throws GameActionException {
        Set<MapLocation> islandLocationsMatch = new HashSet<>();
        Set<MapLocation> islandLocationsNoMatch = new HashSet<>();
        // Read all islands.
        for (int i = START_ISLAND_IDX; i < START_CARRIER_IDX; ++i) {
            int valueToUnpack = rc.readSharedArray(i);
            if (valueToUnpack != 0) {
                // Indices 0 and 1 of unpackedValue are x and y values.
                int[] unpackedValue = Packing.unpackObject(rc, valueToUnpack);
                MapLocation locToAdd = new MapLocation(unpackedValue[0], unpackedValue[1]);
                int typeValue = unpackedValue[2];
                // Break out of loop if type of object is found.
                if (typeValue == type) {
                    islandLocationsMatch.add(locToAdd);
                }
                else {
                    islandLocationsNoMatch.add(locToAdd);
                }
            }
        }
        // Return the closest island or return null.
        if (!islandLocationsMatch.isEmpty()) {
            return Mapping.getClosestLocation(rc, islandLocationsMatch);
        }
        if (!islandLocationsNoMatch.isEmpty()) {
            return Mapping.getClosestLocation(rc, islandLocationsNoMatch);
        }
        return null;
    }

    /** Write island location to array. For Carrier and Launcher use. **/
    public static void writeIslands(RobotController rc) throws GameActionException {
        int[] islands = rc.senseNearbyIslands();
        Team myTeam = rc.getTeam();
        Set<Integer> islandsToStore = new HashSet<>();

        for (int id : islands) {
            // Check if island is occupied first.
            int islandState = Packing.getIslandStateNum(myTeam, rc.senseTeamOccupyingIsland(id));
            MapLocation[] thisIslandLocs = rc.senseNearbyIslandLocations(id);
            // Keep the closest parts of each island.
            MapLocation closestIsland = Mapping.getClosestLocation(rc, thisIslandLocs);
            islandsToStore.add(Packing.packObject(rc, closestIsland, islandState));
        }
        // Add as many islands as possible.
        if (!islandsToStore.isEmpty()) {
            for (Integer island : islandsToStore) {
                // Write location into first available index.
                for (int i = START_ISLAND_IDX; i < START_CARRIER_IDX; ++i) {
                    if (rc.readSharedArray(i) == 0) {
                        if (rc.canWriteSharedArray(i, island)) {
                            rc.writeSharedArray(i, island);
                            break;
                        }
                    }
                    else if (rc.readSharedArray(i) == island) {
                        // If island already exists, skip this iteration.
                        break;
                    }
                }
            }
        }
    }

    /** Update array of islands, removing any that are now occupied. **/
    public static void updateIslands(RobotController rc, MapLocation island, int islandState) throws GameActionException {
        // If island is in array, update it.
        for (int i = START_ISLAND_IDX; i < START_CARRIER_IDX; ++i) {
            int[] islandSpecs = Packing.unpackObject(rc, rc.readSharedArray(i));
            int xCoord = islandSpecs[0];
            int yCoord = islandSpecs[1];
            int stateValue = islandSpecs[2];

            // If island is found and not the same state as the parameter, update it.
            if (xCoord == island.x && yCoord == island.y && stateValue != islandState) {
                int updatedValue = Packing.packObject(rc, island, islandState);
                if (rc.canWriteSharedArray(i, updatedValue)) {
                    rc.writeSharedArray(i, updatedValue);
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
}