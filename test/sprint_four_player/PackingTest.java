package sprint_four_player;

import battlecode.common.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class PackingTest {
    // Test combining x and y coordinates into one value.
    @Test
    public void testPackCoordinatesIntoValue() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        MapLocation location = new MapLocation(1, 2);
        assertEquals(8, Packing.packCoordinates(rc, location));
    }

    // Test getting the x and y coordinate from one value.
    @Test
    public void testUnpackCoordinatesFromValue() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int mapValue = 8;
        assertEquals(new MapLocation(1, 2), Packing.unpackCoordinates(rc, mapValue));
    }

    // Test packing Object information with no type into one value.
    @Test
    public void testPackObjectIntoValueNoType() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        MapLocation locationToPack = new MapLocation(2, 2);
        int packedValue = 144;
        assertEquals(packedValue, Packing.packObject(rc, locationToPack));
    }

    // Test packing Headquarters information into one value.
    @Test
    public void testPackObjectIntoValue() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        MapLocation locationToPack = new MapLocation(2, 2);
        int typeToPack = 2;
        int packedValue = 146;
        int testValue = Packing.packObject(rc, locationToPack, typeToPack);
        assertEquals(packedValue, testValue);
    }

    // Test unpacking Headquarters information from one value.
    @Test
    public void testUnpackObjectFromValue() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int valueToUnpack = 146;
        int[] unpackedValues = Packing.unpackObject(rc, valueToUnpack);
        assertArrayEquals(new int[]{2, 2, 2}, unpackedValues);
    }

    // Test reading the resource type of well if it is any of the three resource types.
    @Test
    public void testReadWellType() {
        assertEquals(1, Packing.getWellTypeNum(ResourceType.ADAMANTIUM));
        assertEquals(2, Packing.getWellTypeNum(ResourceType.MANA));
        assertEquals(3, Packing.getWellTypeNum(ResourceType.ELIXIR));
    }

    // Test reading if an island is unoccupied.
    @Test
    public void testReadUnoccupiedIsland() {
        CommunicationRobotController rc = new CommunicationRobotController();
        assertEquals(0, Packing.getIslandStateNum(rc.getTeam(), Team.NEUTRAL));
    }

    // Test reading if an island is occupied by team.
    @Test
    public void testReadTeamOccupiedIsland() {
        CommunicationRobotController rc = new CommunicationRobotController();
        assertEquals(1, Packing.getIslandStateNum(rc.getTeam(), Team.A));
    }

    // Test reading if an island is occupied by opponent.
    @Test
    public void testReadOpponentOccupiedIsland() {
        CommunicationRobotController rc = new CommunicationRobotController();
        assertEquals(2, Packing.getIslandStateNum(rc.getTeam(), Team.B));
    }
}