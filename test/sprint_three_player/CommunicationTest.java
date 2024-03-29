package sprint_three_player;

import battlecode.common.*;
import battlecode.world.Inventory;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommunicationTest {

    // Testing reading an HQ from an empty shared array.
    @Test
    public void testReadHQWithEmptySharedArray() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        MapLocation closestHQ = Communication.readHQ(rc);
        // Assert that location is null.
        assertNull(closestHQ);
    }

    // Testing reading the closest HQ from a shared array with one HQ.
    @Test
    public void testReadHQWithOneHQ() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] oneHQInArray = new int[] {
                144, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(oneHQInArray);
        MapLocation hqLocation = Communication.readHQ(rc);
        assertEquals(new MapLocation(2, 2), hqLocation);
    }

    // Testing reading the closest HQ from a shared array with multiple HQ's.
    @Test
    public void testReadHQWithMultipleHQ() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] multiHQInArray = new int[] {
                144, 112, 80, 128, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(multiHQInArray);
        MapLocation closestHQ = Communication.readHQ(rc);
        assertEquals(new MapLocation(1, 1), closestHQ);
    }

    // Testing writing an HQ that is null.
    @Test
    public void testWriteNullHQ() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(null);
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        Communication.writeHQ(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing writing an HQ to an empty shared array when it can write.
    @Test
    public void testWriteHQToEmptyArrayCanWrite() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(new MapLocation(1, 1));
        int[] validArray = new int[] {
                80, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        Communication.writeHQ(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing writing an HQ to an empty shared array when it cannot write.
    @Test
    public void testWriteHQToEmptyArrayCannotWrite() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(new MapLocation(1, 1));
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        rc.setCanWriteResult(false);
        Communication.writeHQ(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing writing an HQ to a shared array with one HQ.
    @Test
    public void testWriteHQWithOneHQInArray() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(new MapLocation(1, 1));
        int[] existingArray = new int[] {
                144, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        int[] validArray = new int[] {
                144, 80, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        rc.setSharedArray(existingArray);

        Communication.writeHQ(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing writing an HQ to a shared array with multiple HQ.
    @Test
    public void testWriteHQWithMultipleHQInArray() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(new MapLocation(1, 1));
        int[] existingArray = new int[] {
                144, 112, 128, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        int[] validArray = new int[] {
                144, 112, 128, 80, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        rc.setSharedArray(existingArray);
        Communication.writeHQ(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing reading a well from an empty shared array.
    @Test
    public void testReadWellWithEmptySharedArray() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int typeToFind = 2;
        MapLocation closestWell = Communication.readWell(rc, typeToFind);
        // Assert that location is null.
        assertNull(closestWell);
    }

    // Testing reading the well matching a specific type from a shared array with one well.
    @Test
    public void testReadWellWithOneWellMatchingType() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] oneWellInArray = new int[] {
                0, 0, 0, 0, 82, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(oneWellInArray);
        MapLocation wellResult = Communication.readWell(rc, 2);
        assertEquals(new MapLocation(1, 1), wellResult);
    }

    // Testing reading the well not matching a specific type from a shared array with one well.
    @Test
    public void testReadWellWithOneWellNotMatchingType() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] oneWellInArray = new int[] {
                0, 0, 0, 0, 80, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(oneWellInArray);
        MapLocation wellResult = Communication.readWell(rc, 2);
        assertEquals(new MapLocation(1, 1), wellResult);
    }

    // Testing reading the matching well type from a shared array with multiple wells.
    @Test
    public void testReadWellWithMultipleWellMatchingType() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] multiWellInArray = new int[] {
                0, 0, 0, 0, 146, 114, 131, 81,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(multiWellInArray);
        MapLocation wellResult = Communication.readWell(rc, 1);
        assertEquals(new MapLocation(1, 1), wellResult);
    }

    // Testing reading the well not matching type from a shared array with multiple wells.
    @Test
    public void testReadWellWithMultipleWellNotMatchingType() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] multiWellInArray = new int[] {
                0, 0, 0, 0, 146, 114, 131, 82,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(multiWellInArray);
        MapLocation wellResult = Communication.readWell(rc, 1);
        assertEquals(new MapLocation(1, 1), wellResult);
    }

    // Testing writing a well when none are sensed.
    @Test
    public void testWriteWellsNoneSensed() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setWells(new WellInfo[] {});
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        Communication.writeWells(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing writing a well to an empty shared array when it can write.
    @Test
    public void testWriteWellsToEmptyArrayCanWrite() throws GameActionException {
        CommunicationRobotController rc = getCommunicationRobotController();
        int[] validArray = new int[] {
                0, 0, 0, 0, 81, 130, 147, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        Communication.writeWells(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    private static CommunicationRobotController getCommunicationRobotController() {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setWells(new WellInfo[] {
                new WellInfo(
                        new MapLocation(1, 1),
                        ResourceType.ADAMANTIUM,
                        new Inventory(10),
                        false),
                new WellInfo(
                        new MapLocation(1, 2),
                        ResourceType.MANA,
                        new Inventory(10),
                        false),
                new WellInfo(
                        new MapLocation(2, 2),
                        ResourceType.ELIXIR,
                        new Inventory(10),
                        false)
        });
        return rc;
    }

    // Testing writing a well to an empty shared array when it cannot write.
    @Test
    public void testWriteWellsCannotWrite() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setWells(new WellInfo[] {
                new WellInfo(
                        new MapLocation(1, 1),
                        ResourceType.NO_RESOURCE,
                        new Inventory(10),
                        false),
                new WellInfo(
                        new MapLocation(1, 2),
                        ResourceType.NO_RESOURCE,
                        new Inventory(10),
                        false),
                new WellInfo(
                        new MapLocation(2, 2),
                        ResourceType.NO_RESOURCE,
                        new Inventory(10),
                        false)
        });
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        rc.setCanWriteResult(false);
        Communication.writeWells(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing writing a well to a shared array with a matching location.
    @Test
    public void testWriteWellsExistingWell() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setWells(new WellInfo[] {
                new WellInfo(
                        new MapLocation(1, 1),
                        ResourceType.ADAMANTIUM,
                        new Inventory(10),
                        false),
                new WellInfo(
                        new MapLocation(1, 2),
                        ResourceType.MANA,
                        new Inventory(10),
                        false),
                new WellInfo(
                        new MapLocation(2, 2),
                        ResourceType.ELIXIR,
                        new Inventory(10),
                        false)
        });
        int[] initialArray = new int[] {
                0, 0, 0, 0, 81, 130, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        int[] validArray = new int[] {
                0, 0, 0, 0, 81, 130, 147, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        rc.setSharedArray(initialArray);
        Communication.writeWells(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing reading an island from an empty shared array.
    @Test
    public void testReadIslandWithEmptySharedArray() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        MapLocation closestIsland = Communication.readIsland(rc, 2);
        // Assert that location is null.
        assertNull(closestIsland);
    }

    // Testing reading the island matching a specific type from a shared array with one island.
    @Test
    public void testReadIslandWithOneIslandMatchingType() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] oneWellInArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                82, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(oneWellInArray);
        MapLocation islandResult = Communication.readIsland(rc, 2);
        assertEquals(new MapLocation(1, 1), islandResult);
    }

    // Testing reading the island not matching a specific type from a shared array with one island.
    @Test
    public void testReadIslandWithOneIslandNotMatchingType() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] oneWellInArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                80, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(oneWellInArray);
        MapLocation wellResult = Communication.readIsland(rc, 2);
        assertEquals(new MapLocation(1, 1), wellResult);
    }

    // Testing reading the island matching type from a shared array with multiple islands.
    @Test
    public void testReadIslandWithMultipleIslandMatchingType() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] multiWellInArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                146, 114, 131, 81, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(multiWellInArray);
        MapLocation wellResult = Communication.readIsland(rc, 1);
        assertEquals(new MapLocation(1, 1), wellResult);
    }

    // Testing reading the island not matching type from a shared array with multiple islands.
    @Test
    public void testReadIslandWithMultipleIslandNotMatchingType() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] multiWellInArray = new int[]{
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                146, 114, 131, 82, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(multiWellInArray);
        MapLocation wellResult = Communication.readIsland(rc, 1);
        assertEquals(new MapLocation(1, 1), wellResult);
    }

    // Testing writing an island with no islands sensed.
    @Test
    public void testWriteIslandsNoIslandsSensed() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setCurrentIslands(new int[]{});
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        Communication.writeIslands(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing writing island when robot cannot write.
    @Test
    public void testWriteIslandsCannotWrite() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setCanWriteResult(false);
        Communication.writeIslands(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing writing island when robot can write.
    @Test
    public void testWriteIslandsCanWrite() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                80, 129, 146, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        Communication.writeIslands(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing writing island when island already exists.
    @Test
    public void testWriteIslandsExistingIsland() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] initialArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                80, 146, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                80, 146, 129, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(initialArray);
        Communication.writeIslands(rc);
        assertArrayEquals(validArray, rc.getArray());
        rc.reset();
    }

    // Testing updating islands when island does not exist.
    @Test
    public void testUpdateIslandsDoesNotExist() throws GameActionException{
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        Communication.updateIslands(rc, new MapLocation(1, 1), 2);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing updating islands when robot cannot write.
    @Test
    public void testUpdateIslandsCannotWrite() throws GameActionException{
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                80, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(validArray);
        rc.setCanWriteResult(false);
        Communication.updateIslands(rc, new MapLocation(1, 1), 1);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing updating islands when robot can write.
    @Test
    public void testUpdateIslandsCanWrite() throws GameActionException{
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] initialArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                80, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                81, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(initialArray);
        Communication.updateIslands(rc, new MapLocation(1, 1), 1);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing read priority returning the correct value from the shared array.
    @Test
    public void testReadPriority() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] initialArray = new int[] {
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 2
        };
        rc.setSharedArray(initialArray);
        int priorityResult = Communication.readPriority(rc);
        assertEquals(2, priorityResult);
    }

    // Testing writing priority when robot cannot write.
    @Test
    public void testWritePriorityCannotWrite() throws GameActionException{
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setCanWriteResult(false);
        Communication.writePriority(rc, 1);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing writing priority when robot can write.
    @Test
    public void testWritePriorityCanWrite() throws GameActionException{
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] initialArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 1
        };
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 2
        };
        rc.setSharedArray(initialArray);
        Communication.writePriority(rc, 2);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Test combining x and y coordinates into one value.
    @Test
    public void testPackCoordinatesIntoValue() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        MapLocation location = new MapLocation(1, 2);
        assertEquals(8, Communication.packCoordinates(rc, location));
    }

    // Test getting the x and y coordinate from one value.
    @Test
    public void testUnpackCoordinatesFromValue() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int mapValue = 8;
        assertEquals(new MapLocation(1, 2), Communication.unpackCoordinates(rc, mapValue));
    }

    // Test packing Object information with no type into one value.
    @Test
    public void testPackObjectIntoValueNoType() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        MapLocation locationToPack = new MapLocation(2, 2);
        int packedValue = 144;
        assertEquals(packedValue, Communication.packObject(rc, locationToPack));
    }

    // Test packing Headquarters information into one value.
    @Test
    public void testPackObjectIntoValue() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        MapLocation locationToPack = new MapLocation(2, 2);
        int typeToPack = 2;
        int packedValue = 146;
        int testValue = Communication.packObject(rc, locationToPack, typeToPack);
        assertEquals(packedValue, testValue);
    }

    // Test unpacking Headquarters information from one value.
    @Test
    public void testUnpackObjectFromValue() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int valueToUnpack = 146;
        int[] unpackedValues = Communication.unpackObject(rc, valueToUnpack);
        assertArrayEquals(new int[]{2, 2, 2}, unpackedValues);
    }

    // Test reading the resource type of well if it is any of the three resource types.
    @Test
    public void testReadWellType() {
        assertEquals(1, Communication.wellTypeNum(ResourceType.ADAMANTIUM));
        assertEquals(2, Communication.wellTypeNum(ResourceType.MANA));
        assertEquals(3, Communication.wellTypeNum(ResourceType.ELIXIR));
    }

    // Test reading if an island is unoccupied.
    @Test
    public void testReadUnoccupiedIsland() {
        CommunicationRobotController rc = new CommunicationRobotController();
        assertEquals(0, Communication.islandStateNum(rc.getTeam(), Team.NEUTRAL));
    }

    // Test reading if an island is occupied by team.
    @Test
    public void testReadTeamOccupiedIsland() {
        CommunicationRobotController rc = new CommunicationRobotController();
        assertEquals(1, Communication.islandStateNum(rc.getTeam(), Team.A));
    }

    // Test reading if an island is occupied by opponent.
    @Test
    public void testReadOpponentOccupiedIsland() {
        CommunicationRobotController rc = new CommunicationRobotController();
        assertEquals(2, Communication.islandStateNum(rc.getTeam(), Team.B));
    }

    // Testing reading a Carrier from an empty shared array.
    @Test
    public void testReadCarrierWithEmptySharedArray() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        MapLocation closestCarrier = Communication.readCarrier(rc);
        // Assert that location is null.
        assertNull(closestCarrier);
    }

    // Testing reading the closest Carrier from a shared array with one Carrier.
    @Test
    public void testReadCarrierWithOneCarrier() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] oneCarrierInArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 144, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(oneCarrierInArray);
        MapLocation carrierLocation = Communication.readCarrier(rc);
        assertEquals(new MapLocation(2, 2), carrierLocation);
    }

    // Testing reading the closest Carrier from a shared array with multiple Carrier's.
    @Test
    public void testReadCarrierWithMultipleCarrier() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] multiCarrierInArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 112, 80,
                128, 144, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(multiCarrierInArray);
        MapLocation closestCarrier = Communication.readCarrier(rc);
        assertEquals(new MapLocation(1, 1), closestCarrier);
    }

    // Testing initializing a Carrier that is null.
    @Test
    public void testInitializeNullCarrier() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(null);
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        Communication.initializeCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing initializing a Carrier to an empty shared array when it can write.
    @Test
    public void testInitializeCarrierToEmptyArrayCanWrite() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(new MapLocation(1, 1));
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 80, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        Communication.initializeCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
        Communication.removeAllCarrierIDs();
    }

    // Testing initializing a Carrier to an empty shared array when it cannot write.
    @Test
    public void testInitializeCarrierToEmptyArrayCannotWrite() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(new MapLocation(1, 1));
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        rc.setCanWriteResult(false);
        Communication.initializeCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing initializing a Carrier to a shared array with one Carrier.
    @Test
    public void testInitializeCarrierWithOneCarrierInArray() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(new MapLocation(2, 2));
        rc.setMyID(11000);
        Communication.addCarrierID(rc);
        rc.setLocation(new MapLocation(1, 1));
        rc.setMyID(12000);
        int[] existingArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 144, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 144, 80,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        rc.setSharedArray(existingArray);

        Communication.initializeCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
        Communication.removeAllCarrierIDs();
    }

    // Testing initializing a Carrier to a shared array with multiple Carrier.
    @Test
    public void testInitializeCarrierWithMultipleCarrierInArray() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(new MapLocation(2, 2));
        rc.setMyID(11000);
        Communication.addCarrierID(rc);
        rc.setLocation(new MapLocation(1, 2));
        rc.setMyID(12000);
        Communication.addCarrierID(rc);
        rc.setLocation(new MapLocation(2, 1));
        rc.setMyID(13000);
        Communication.addCarrierID(rc);
        rc.setLocation(new MapLocation(1, 1));
        rc.setMyID(14000);
        int[] existingArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 144, 112,
                128, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 144, 112,
                128, 80, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        rc.setSharedArray(existingArray);
        Communication.initializeCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
        Communication.removeAllCarrierIDs();
    }

    // Testing initializing a Carrier to a shared array that is full.
    @Test
    public void testInitializeCarrierFull() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        for (int i = 0; i < 17; ++i) {
            rc.setMyID(14000 + i);
            Communication.addCarrierID(rc);
        }
        rc.setMyID(16000);
        rc.setLocation(new MapLocation(1, 1));
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 144, 112,
                128, 23, 24, 25, 26, 48, 94, 99,
                54, 30, 1, 2, 1000, 939, 324, 0
        };
        rc.setSharedArray(validArray);
        Communication.initializeCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
        Communication.removeAllCarrierIDs();
    }

    // Testing updating a Carrier that is null.
    @Test
    public void testUpdateNullCarrier() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(null);
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        Communication.updateCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing updating a Carrier that does not exist.
    @Test
    public void testUpdateCarrierDoesNotExist() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setMyID(11000);
        Communication.addCarrierID(rc);
        rc.setMyID(14000);
        rc.setLocation(new MapLocation(1, 1));
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 144, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(validArray);
        Communication.updateCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
        Communication.removeAllCarrierIDs();
    }

    // Testing updating a Carrier cannot write.
    @Test
    public void testUpdateCarrierCannotWrite() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setMyID(11000);
        Communication.addCarrierID(rc);
        rc.setLocation(new MapLocation(1, 1));
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 144, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(validArray);
        rc.setCanWriteResult(false);
        Communication.updateCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
        Communication.removeAllCarrierIDs();
    }

    // Testing updating a Carrier can write.
    @Test
    public void testUpdateCarrierCanWrite() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setMyID(11000);
        Communication.addCarrierID(rc);
        rc.setLocation(new MapLocation(1, 1));
        int[] existingArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 144, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 80, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(existingArray);
        Communication.updateCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
        Communication.removeAllCarrierIDs();
    }

    // Testing removing carriers when carrier does not exist.
    @Test
    public void testRemoveCarriersDoesNotExist() throws GameActionException{
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        Communication.removeCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing removing carriers when robot cannot write.
    @Test
    public void testRemoveCarriersCannotWrite() throws GameActionException{
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 80, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(validArray);
        rc.setCanWriteResult(false);
        Communication.removeCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing remove carriers when robot can write.
    @Test
    public void testUpdateCarriersCanWrite() throws GameActionException{
        CommunicationRobotController rc = new CommunicationRobotController();
        Communication.addCarrierID(rc);
        int[] initialArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 80, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        int[] validArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(initialArray);
        Communication.removeCarrier(rc);
        assertArrayEquals(validArray, rc.getArray());
    }

    // Testing add a Carrier's ID to tracking array can add.
    @Test
    public void testCanAddCarrierID() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        assertTrue(Communication.addCarrierID(rc));
        Communication.removeCarrierID(rc);
    }

    // Testing add a Carrier's ID to tracking array cannot add.
    @Test
    public void testCannotAddCarrierID() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        boolean result = true;
        for (int i = 0; i < 17; ++i) {
            rc.setMyID(13000 + i);
            result = Communication.addCarrierID(rc);
        }
        assertTrue(result);
        rc.setMyID(15000);
        assertFalse(Communication.addCarrierID(rc));
        for (int i = 0; i < 17; ++i) {
            rc.setMyID(13000 + i);
            Communication.removeCarrierID(rc);
        }
    }

    // Testing getting a Carrier's index from tracking array.
    @Test
    public void testGetCarrierIndex() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        assertEquals(-1, Communication.getCarrierIndex(rc));
        boolean result = Communication.addCarrierID(rc);
        assertTrue(result);
        assertEquals(0, Communication.getCarrierIndex(rc));
        Communication.removeCarrierID(rc);
    }

    // Testing removing and not getting a Carrier's ID to tracking array.
    @Test
    public void testRemoveAndNotGetCarrierID() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        boolean result = Communication.addCarrierID(rc);
        assertTrue(result);
        assertEquals(0, Communication.getCarrierIndex(rc));
        Communication.removeCarrierID(rc);
        assertEquals(-1, Communication.getCarrierIndex(rc));
    }

    // Testing removing all carriers from ID tracking array.
    @Test
    public void testRemoveAllCarrierIDs() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        for (int i = 0; i < 17; ++i) {
            rc.setMyID(14000 + i);
            assertTrue(Communication.addCarrierID(rc));
        }
        for (int i = 0; i < 17; ++i) {
            rc.setMyID(14000 + i);
            assertNotEquals(-1, Communication.getCarrierIndex(rc));
        }
        Communication.removeAllCarrierIDs();
        for (int i = 0; i < 17; ++i) {
            rc.setMyID(14000 + i);
            assertEquals(-1, Communication.getCarrierIndex(rc));
        }
    }
}

/**
 * Implements a simple mock RobotController for testing. Has to implement all methods, but the only affected methods
 * are getMapWidth, getMapHeight, getID, getLocation, getTeam, senseNearbyWells, senseNearbyIslands, senseTeamOccupyingIsland,
 * and senseNearbyIslandLocations. New methods for testing include setSharedArray, setCanWriteResult, setLocation, getArray,
 * setWells, setCurrentIslands, setCurrentTeam, setCurrentIslandLocations, setTeam, reset, and setMyID.
 **/
class CommunicationRobotController implements RobotController {
    MapLocation currentLocation = new MapLocation(0, 0);
    WellInfo[] currentWells = new WellInfo[] {};
    Team myTeam = Team.A;
    int[] currentIslands = new int[] {0, 1, 2};
    int myID = 15421;
    Team[] currentTeam = new Team[] {Team.NEUTRAL, Team.A, Team.B};
    MapLocation[] islandLocations = new MapLocation[] {new MapLocation(1, 1), new MapLocation(1, 2), new MapLocation(2, 2)};
    boolean canWriteResult = true;

    // Create a sharedArray of 64 integers.
    int[] sharedArray = new int[] {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    public void setCurrentIslands(int[] islandsToSet) {
        currentIslands = islandsToSet;
    }

    public void setSharedArray(int[] sharedArrayResult) {
        sharedArray = sharedArrayResult.clone();
    }

    public void setCanWriteResult(boolean writeResult) {
        canWriteResult = writeResult;
    }

    public void setLocation(MapLocation location) {
        currentLocation = location;
    }

    public void setWells(WellInfo[] wellInfo) {
        currentWells = wellInfo;
    }

    public int[] getArray() {
        return sharedArray;
    }

    public void reset() {
        canWriteResult = true;
        currentLocation = new MapLocation(0, 0);
        currentWells = new WellInfo[]{};
        currentIslands = new int[] {0, 1, 2};
        currentTeam = new Team[] {Team.NEUTRAL, Team.NEUTRAL, Team.NEUTRAL};
        islandLocations = new MapLocation[] {new MapLocation(1, 1), new MapLocation(1, 2), new MapLocation(2, 2)};
        sharedArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
    }

    public void setMyID(int id) {
        myID = id;
    }

    @Override
    public Team getTeam() {
        return myTeam;
    }

    @Override
    public int getID() {
        return myID;
    }

    @Override
    public int[] senseNearbyIslands() {
        return currentIslands;
    }

    @Override
    public MapLocation[] senseNearbyIslandLocations(int idx) {
        return new MapLocation[] {islandLocations[idx]};
    }

    @Override
    public Team senseTeamOccupyingIsland(int islandIdx)  {
        return currentTeam[islandIdx];
    }

    @Override
    public WellInfo[] senseNearbyWells() {
        return currentWells;
    }

    @Override
    public int getMapWidth() {
        return 3;
    }

    @Override
    public int getMapHeight() {
        return 3;
    }

    @Override
    public MapLocation getLocation() {
        return currentLocation;
    }

    @Override
    public int readSharedArray(int index)  {
        return sharedArray[index];
    }

    // The games indices and values are always within valid parameters, so passed in values cannot be tested.
    @Override
    public boolean canWriteSharedArray(int index, int value) {
        return canWriteResult;
    }

    @Override
    public void writeSharedArray(int index, int value)  {
        sharedArray[index] = value;
    }


    @Override
    public boolean canMove(Direction dir) {
        return true;
    }

    @Override
    public void move(Direction dir) {
    }

    @Override
    public int getRoundNum() {
        return 0;
    }

    @Override
    public int getIslandCount() {
        return 0;
    }

    @Override
    public int getRobotCount() {
        return 0;
    }

    @Override
    public RobotType getType() {
        return null;
    }

    @Override
    public int getHealth() {
        return 0;
    }

    @Override
    public int getResourceAmount(ResourceType rType) {
        return 0;
    }

    @Override
    public Anchor getAnchor()  {
        return null;
    }

    @Override
    public int getNumAnchors(Anchor anchorType) {
        return 0;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public boolean onTheMap(MapLocation loc) {
        return false;
    }

    @Override
    public boolean canSenseLocation(MapLocation loc) {
        return false;
    }

    @Override
    public boolean canActLocation(MapLocation loc) {
        return false;
    }

    @Override
    public boolean isLocationOccupied(MapLocation loc)  {
        return false;
    }

    @Override
    public boolean canSenseRobotAtLocation(MapLocation loc) {
        return false;
    }

    @Override
    public RobotInfo senseRobotAtLocation(MapLocation loc)  {
        return null;
    }

    @Override
    public boolean canSenseRobot(int id) {
        return false;
    }

    @Override
    public RobotInfo senseRobot(int id)  {
        return null;
    }

    @Override
    public RobotInfo[] senseNearbyRobots() {
        return new RobotInfo[0];
    }

    @Override
    public RobotInfo[] senseNearbyRobots(int radiusSquared)  {
        return new RobotInfo[0];
    }

    @Override
    public RobotInfo[] senseNearbyRobots(int radiusSquared, Team team)  {
        return new RobotInfo[0];
    }

    @Override
    public RobotInfo[] senseNearbyRobots(MapLocation center, int radiusSquared, Team team)  {
        return new RobotInfo[0];
    }

    @Override
    public boolean sensePassability(MapLocation loc)  {
        return false;
    }

    @Override
    public int senseIsland(MapLocation loc)  {
        return 0;
    }

    @Override
    public MapLocation[] senseNearbyIslandLocations(int radiusSquared, int idx)  {
        return new MapLocation[0];
    }

    @Override
    public MapLocation[] senseNearbyIslandLocations(MapLocation center, int radiusSquared, int idx)  {
        return new MapLocation[0];
    }

    @Override
    public int senseAnchorPlantedHealth(int islandIdx)  {
        return 0;
    }

    @Override
    public Anchor senseAnchor(int islandIdx)  {
        return null;
    }

    @Override
    public boolean senseCloud(MapLocation loc)  {
        return false;
    }

    @Override
    public MapLocation[] senseNearbyCloudLocations() {
        return new MapLocation[0];
    }

    @Override
    public MapLocation[] senseNearbyCloudLocations(int radiusSquared)  {
        return new MapLocation[0];
    }

    @Override
    public MapLocation[] senseNearbyCloudLocations(MapLocation center, int radiusSquared)  {
        return new MapLocation[0];
    }

    @Override
    public WellInfo senseWell(MapLocation loc)  {
        return null;
    }

    @Override
    public WellInfo[] senseNearbyWells(int radiusSquared)  {
        return new WellInfo[0];
    }

    @Override
    public WellInfo[] senseNearbyWells(MapLocation center, int radiusSquared)  {
        return new WellInfo[0];
    }

    @Override
    public WellInfo[] senseNearbyWells(ResourceType resourceType) {
        return new WellInfo[0];
    }

    @Override
    public WellInfo[] senseNearbyWells(int radiusSquared, ResourceType resourceType)  {
        return new WellInfo[0];
    }

    @Override
    public WellInfo[] senseNearbyWells(MapLocation center, int radiusSquared, ResourceType resourceType)  {
        return new WellInfo[0];
    }

    @Override
    public MapInfo senseMapInfo(MapLocation loc)  {
        return null;
    }

    @Override
    public MapInfo[] senseNearbyMapInfos() {
        return new MapInfo[0];
    }

    @Override
    public MapInfo[] senseNearbyMapInfos(int radiusSquared)  {
        return new MapInfo[0];
    }

    @Override
    public MapInfo[] senseNearbyMapInfos(MapLocation center)  {
        return new MapInfo[0];
    }

    @Override
    public MapInfo[] senseNearbyMapInfos(MapLocation center, int radiusSquared)  {
        return new MapInfo[0];
    }

    @Override
    public MapLocation adjacentLocation(Direction dir) {
        return null;
    }

    @Override
    public MapLocation[] getAllLocationsWithinRadiusSquared(MapLocation center, int radiusSquared)  {
        return new MapLocation[0];
    }

    @Override
    public boolean isActionReady() {
        return false;
    }

    @Override
    public int getActionCooldownTurns() {
        return 0;
    }

    @Override
    public boolean isMovementReady() {
        return false;
    }

    @Override
    public int getMovementCooldownTurns() {
        return 0;
    }

    @Override
    public boolean canBuildRobot(RobotType type, MapLocation loc) {
        return false;
    }

    @Override
    public void buildRobot(RobotType type, MapLocation loc)  {

    }

    @Override
    public boolean canAttack(MapLocation loc) {
        return false;
    }

    @Override
    public void attack(MapLocation loc)  {

    }

    @Override
    public boolean canBoost() {
        return false;
    }

    @Override
    public void boost()  {

    }

    @Override
    public boolean canDestabilize(MapLocation loc) {
        return false;
    }

    @Override
    public void destabilize(MapLocation loc)  {

    }

    @Override
    public boolean canCollectResource(MapLocation loc, int amount) {
        return false;
    }

    @Override
    public void collectResource(MapLocation loc, int amount)  {

    }

    @Override
    public boolean canTransferResource(MapLocation loc, ResourceType rType, int amount) {
        return false;
    }

    @Override
    public void transferResource(MapLocation loc, ResourceType rType, int amount)  {

    }

    @Override
    public boolean canBuildAnchor(Anchor anchor) {
        return false;
    }

    @Override
    public void buildAnchor(Anchor anchor)  {

    }

    @Override
    public boolean canTakeAnchor(MapLocation loc, Anchor anchorType) {
        return false;
    }

    @Override
    public void takeAnchor(MapLocation loc, Anchor anchorType)  {

    }

    @Override
    public boolean canReturnAnchor(MapLocation loc) {
        return false;
    }

    @Override
    public void returnAnchor(MapLocation loc)  {

    }

    @Override
    public boolean canPlaceAnchor() {
        return false;
    }

    @Override
    public void placeAnchor()  {

    }

    @Override
    public void disintegrate() {

    }

    @Override
    public void resign() {

    }

    @Override
    public void setIndicatorString(String string) {

    }

    @Override
    public void setIndicatorDot(MapLocation loc, int red, int green, int blue) {

    }

    @Override
    public void setIndicatorLine(MapLocation startLoc, MapLocation endLoc, int red, int green, int blue) {

    }

}
