package sprint_three_player;

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

    // Bit shift amount for storage.
    private static final int BIT_SHIFT = 4;

    // Array to hold ID'd carriers up to number of max carriers in shared array (for tracking location).
    private static int[] carrierIDs = new int[NUM_CARRIERS];

    // Counters for objects in shared array.
    private static int well_count = 0;
    private static int island_count = 0;

    /** Read headquarter location closest to robot. **/
    public static MapLocation readHQ(RobotController rc) throws GameActionException {
        Set<MapLocation> hqLocations = new HashSet<>();
        // Read all headquarters.
        for (int i = START_HQ_IDX; i < START_WELL_IDX; ++i) {
            int valueToUnpack = rc.readSharedArray(i);
            if (valueToUnpack != 0) {
                int[] unpackedValue = unpackObject(rc, valueToUnpack);
                int hqX = unpackedValue[0];
                int hqY = unpackedValue[1];
                hqLocations.add(new MapLocation(hqX, hqY));
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
        if (hqLoc != null) {
            int packedValue = packObject(rc, hqLoc);
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
        Set<MapLocation> wellLocations = new HashSet<>();
        // Read all wells.
        for (int i = START_WELL_IDX; i < START_ISLAND_IDX; ++i) {
            int valueToUnpack = rc.readSharedArray(i);
            if (valueToUnpack != 0) {
                int[] unpackedValue = unpackObject(rc, valueToUnpack);
                int xCoord = unpackedValue[0];
                int yCoord = unpackedValue[1];
                int typeValue = unpackedValue[2];
                // Break out of loop if type of object is found.
                if (typeValue == type) {
                    return new MapLocation(xCoord, yCoord);
                }
                wellLocations.add(new MapLocation(xCoord, yCoord));
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
            int[] wellSpecs = new int[wells.length];
            // For as many objects as possible, add them to array.
            for (int i = 0; i < wellSpecs.length; ++i) {
                int type = wellTypeNum(wells[i].getResourceType());
                wellSpecs[i] = packObject(rc, wells[i].getMapLocation(), type);
                // Write location into first available index.
                for (int j = START_WELL_IDX; j < START_ISLAND_IDX; ++j) {
                    if (rc.readSharedArray(j) == 0) {
                        if (rc.canWriteSharedArray(j, wellSpecs[i])) {
                            rc.writeSharedArray(j, wellSpecs[i]);
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
        Set<MapLocation> islandLocations = new HashSet<>();
        // Read all islands.
        for (int i = START_ISLAND_IDX; i < START_CARRIER_IDX; ++i) {
            int valueToUnpack = rc.readSharedArray(i);
            if (valueToUnpack != 0) {
                int[] unpackedValue = unpackObject(rc, valueToUnpack);
                int xCoord = unpackedValue[0];
                int yCoord = unpackedValue[1];
                int typeValue = unpackedValue[2];
                // Break out of loop if type of object is found.
                if (typeValue == type) {
                    return new MapLocation(xCoord, yCoord);
                }
                islandLocations.add(new MapLocation(xCoord, yCoord));
            }
        }
        // Return the closest island or return null.
        return Movement.getClosestLocation(rc, islandLocations);
    }

    /** Write island location to array. For Carrier and Launcher use. **/
    public static void writeIslands(RobotController rc) throws GameActionException {
        int[] islands = rc.senseNearbyIslands();
        Team myTeam = rc.getTeam();
        Set<Integer> islandsToStore = new HashSet<>();

        for (int id : islands) {
            // Check if island is occupied first.
            int islandState = islandStateNum(myTeam, rc.senseTeamOccupyingIsland(id));
            MapLocation[] thisIslandLocs = rc.senseNearbyIslandLocations(id);
            // Keep the closest parts of each island.
            MapLocation closestIsland = Movement.getClosestLocation(rc, thisIslandLocs);
            islandsToStore.add(packObject(rc, closestIsland, islandState));
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
            int[] islandSpecs = unpackObject(rc, rc.readSharedArray(i));
            int xCoord = islandSpecs[0];
            int yCoord = islandSpecs[1];
            int stateValue = islandSpecs[2];

            // If island is found and not the same state as the parameter, update it.
            if (xCoord == island.x && yCoord == island.y && stateValue != islandState) {
                int updatedValue = packObject(rc, island, islandState);
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

    /** Pack object information into one representative value with no type value **/
    public static int packObject(RobotController rc, MapLocation location) throws GameActionException {
        int packedCoordinates = packCoordinates(rc, location);
        return (packedCoordinates & 0xFFF) << BIT_SHIFT;
    }

    /** Pack object information into one representative value (HQ, well, island) **/
    public static int packObject(RobotController rc, MapLocation location, int type) throws GameActionException {
        int packedCoordinates = packCoordinates(rc, location);
        return (packedCoordinates & 0xFFF) << BIT_SHIFT | (type & 0xF);
    }

    /** Unpack object information from one representative value **/
    public static int[] unpackObject(RobotController rc, int valueToUnpack) throws GameActionException {
        int mapValue = (valueToUnpack >> BIT_SHIFT) & 0xFFF;
        int typeValue = valueToUnpack & 0xF;
        MapLocation mapLocation = unpackCoordinates(rc, mapValue);
        return new int[] {mapLocation.x, mapLocation.y, typeValue};
    }

    /** Return number to represent well type **/
    public static int wellTypeNum(ResourceType wellType) {
        if (wellType == ResourceType.ADAMANTIUM) {
            return 1;
        }
        if (wellType == ResourceType.MANA) {
            return 2;
        }
        return 3; // Must be Elixir
    }

    /** Return number to represent island state **/
    public static int islandStateNum(Team myTeam, Team islandTeam) {
        if (islandTeam == Team.NEUTRAL) {
            return 0;
        }
        if (myTeam == islandTeam) {
            return 1;
        }
        return 2;
    }

    /** Read carrier location closest to robot. **/
    public static MapLocation readCarrier(RobotController rc) throws GameActionException {
        Set<MapLocation> carrierLocs = new HashSet<>();
        // Read all carriers.
        for (int i = START_CARRIER_IDX; i < PRIORITY_IDX; ++i) {
            int valueToUnpack = rc.readSharedArray(i);
            if (valueToUnpack != 0) {
                int[] unpackedValue = unpackObject(rc, valueToUnpack);
                int hqX = unpackedValue[0];
                int hqY = unpackedValue[1];
                carrierLocs.add(new MapLocation(hqX, hqY));
            }
        }
        // Return the closest carrier or return null.
        return Movement.getClosestLocation(rc, carrierLocs);
    }

    /**
     * Write carrier location to array when carrying anchor.
     * Reserved for Carrier use only, so no sensing is required.
     * **/
    public static void writeCarrier(RobotController rc) throws GameActionException {
        MapLocation carrierLoc = rc.getLocation();
        if (carrierLoc != null) {
            int packedValue = packObject(rc, carrierLoc);
            // Write location into first available index.
            for (int i = START_CARRIER_IDX; i < PRIORITY_IDX; ++i) {
                if (rc.readSharedArray(i) == 0 && rc.canWriteSharedArray(i, packedValue)) {
                    rc.writeSharedArray(i, packedValue);
                    break;
                }
            }
        }
    }

    /** Update array of carriers, removing any that no longer have an anchor. **/
    public static void updateCarriers(RobotController rc, MapLocation carrierLoc) throws GameActionException {
        // If carrierLoc is in array, remove it.
        for (int i = START_CARRIER_IDX; i < PRIORITY_IDX; ++i) {
            int[] islandSpecs = unpackObject(rc, rc.readSharedArray(i));
            int xCoord = islandSpecs[0];
            int yCoord = islandSpecs[1];

            // If carrierLoc is found and not the same state as the parameter, update it.
            if (xCoord == carrierLoc.x && yCoord == carrierLoc.y) {
                if (rc.canWriteSharedArray(i, 0)) {
                    rc.writeSharedArray(i, 0);
                }
            }
        }
    }

    /** Add carrier ID to array of carrier ID's **/
    public static boolean addCarrierID(RobotController rc) throws GameActionException {
        int id = rc.getID();
        for (int i = 0; i < NUM_CARRIERS; ++i) {
            if (carrierIDs[i] == 0) {
                carrierIDs[i] = id;
                return true;
            }
        }
        return false;
    }

    /** Get index of carrier within ID tracking array **/
    public static int getCarrierIndex(RobotController rc) throws GameActionException {
        int id = rc.getID();
        for (int i = 0; i < NUM_CARRIERS; ++i) {
            if (carrierIDs[i] == id) {
                return i;
            }
        }
        return -1;
    }

    /** Remove carrier from ID tracking array **/
    public static void removeCarrierID(RobotController rc) throws GameActionException {
        int id = rc.getID();
        for (int i = 0; i < NUM_CARRIERS; ++i) {
            if (carrierIDs[i] == id) {
                carrierIDs[i] = 0;
                break;
            }
        }
    }
}