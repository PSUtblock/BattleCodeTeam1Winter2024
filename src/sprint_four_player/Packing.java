package sprint_four_player;

import battlecode.common.*;

import java.util.HashSet;

public class Packing {
    private static final int BIT_SHIFT = 4; // Bit shift amount for storage.

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
    public static int getWellTypeNum(ResourceType wellType) {
        if (wellType == ResourceType.ADAMANTIUM) {
            return 1;
        }
        if (wellType == ResourceType.MANA) {
            return 2;
        }
        return 3; // Must be Elixir
    }

    /** Return number to represent island state **/
    public static int getIslandStateNum(Team myTeam, Team islandTeam) {
        if (islandTeam == Team.NEUTRAL) {
            return 0;
        }
        if (myTeam == islandTeam) {
            return 1;
        }
        return 2;
    }
}
