package sprint_four_player;

import battlecode.common.*;

public class Packing {
    private static final int BIT_SHIFT = 4; // Bit shift amount for storage.

    /** Pack coordinates into one representative value **/
    public static int packCoordinates(RobotController rc, MapLocation location) {
        return 1 + location.x + location.y * rc.getMapWidth();
    }

    /** Unpack coordinates from one representative value **/
    public static MapLocation unpackCoordinates(RobotController rc, int mapValue) {
        int x = (mapValue - 1) % rc.getMapWidth();
        int y = (mapValue - 1) / rc.getMapWidth();
        return new MapLocation(x, y);
    }

    /** Pack object information into one representative value with no type value **/
    public static int packObject(RobotController rc, MapLocation location) {
        return packObject(rc, location, 0);
    }

    /** Pack object information into one representative value (HQ, well, island) **/
    public static int packObject(RobotController rc, MapLocation location, int type) {
        int packedCoordinates = packCoordinates(rc, location);
        return (packedCoordinates & 0xFFF) << BIT_SHIFT | (type & 0xF);
    }

    /** Unpack object information from one representative value **/
    public static int[] unpackObject(RobotController rc, int valueToUnpack) {
        int mapValue = (valueToUnpack >> BIT_SHIFT) & 0xFFF;
        int typeValue = valueToUnpack & 0xF;
        MapLocation mapLocation = unpackCoordinates(rc, mapValue);
        return new int[] {mapLocation.x, mapLocation.y, typeValue};
    }

    /** Return number to represent well type **/
    public static int getWellTypeNum(ResourceType wellType) {
        switch (wellType) {
            case ADAMANTIUM:
                return 1;
            case MANA:
                return 2;
            default:
                return 3; // Elixir
        }
    }

    /** Return number to represent island state **/
    public static int getIslandStateNum(Team myTeam, Team islandTeam) {
        if (islandTeam == Team.NEUTRAL) {
            return 0;
        }
        return (myTeam == islandTeam) ? 1 : 2;
    }
}
