package sprint_two_player;

import battlecode.common.*;
import battlecode.world.Inventory;

import static org.junit.Assert.*;

import org.junit.Test;

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
                1, 1, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(oneHQInArray);
        MapLocation closestHQ = Communication.readHQ(rc);
        // Assert that closest location is correct.
        assertEquals(new MapLocation(1, 1), closestHQ);
        rc.reset();
    }

    // Testing reading the closest HQ from a shared array with multiple HQ's.
    @Test
    public void testReadHQWithMultipleHQ() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] multiHQInArray = new int[] {
                2, 2, 2, 0, 1, 1, 1, 2,
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
        // Assert that closest location is correct.
        assertEquals(new MapLocation(1, 1), closestHQ);
        rc.reset();
    }

    // Testing writing an HQ to an empty shared array when it can write.
    @Test
    public void testWriteHQToEmptyArrayCanWrite() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(new MapLocation(1, 1));
        int[] validArray = new int[] {
                1, 1, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        rc.setCanWriteResult(true);
        Communication.writeHQ(rc);
        // Assert that written HQ is equal to validArray.
        assertArrayEquals(validArray, rc.getArray());
        rc.reset();
    }

    // Testing writing a null HQ to an empty shared array.
    @Test
    public void testWriteNullHQToEmptyArray() throws GameActionException {
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

        rc.setCanWriteResult(true);
        Communication.writeHQ(rc);
        // Assert that written HQ is equal to validArray.
        assertArrayEquals(validArray, rc.getArray());
        rc.reset();
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
        // Assert that written HQ is equal to validArray.
        assertArrayEquals(validArray, rc.getArray());
        rc.reset();
    }

    // Testing writing an HQ to a shared array with one HQ.
    // HQ's never spawn in the same location, so no need to test location validity.
    @Test
    public void testWriteHQWithOneHQInArray() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(new MapLocation(1, 1));
        int[] existingArray = new int[] {
                2, 2, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        int[] validArray = new int[] {
                2, 2, 1, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        rc.setSharedArray(existingArray);

        rc.setCanWriteResult(true);
        Communication.writeHQ(rc);
        // Assert that written HQ is equal to validArray.
        assertArrayEquals(validArray, rc.getArray());
        rc.reset();
    }

    // Testing writing an HQ to a shared array with multiple HQ.
    // HQ's never spawn in the same location, so no need to test location validity.
    // No more than four HQ's will ever be created, so no need to test for overfilling.
    @Test
    public void testWriteHQWithMultipleHQInArray() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        rc.setLocation(new MapLocation(1, 1));
        int[] existingArray = new int[] {
                2, 2, 1, 2, 1, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        int[] validArray = new int[] {
                2, 2, 1, 2, 1, 0, 1, 1,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        rc.setSharedArray(existingArray);

        rc.setCanWriteResult(true);
        Communication.writeHQ(rc);
        // Assert that written HQ is equal to validArray.
        assertArrayEquals(validArray, rc.getArray());
        rc.reset();
    }

    // Testing reading a well from an empty shared array.
    @Test
    public void testReadWellWithEmptySharedArray() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        MapLocation closestWell = Communication.readWell(rc);
        // Assert that location is null.
        assertNull(closestWell);
    }

    // Testing reading the closest well from a shared array with one well.
    @Test
    public void testReadWellWithOneWell() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] oneWellInArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 1, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(oneWellInArray);
        MapLocation closestWell = Communication.readWell(rc);
        // Assert that closest location is correct.
        assertEquals(new MapLocation(1, 1), closestWell);
        rc.reset();
    }

    // Testing reading the closest well from a shared array with multiple wells.
    @Test
    public void testReadWellWithMultipleWell() throws GameActionException {
        CommunicationRobotController rc = new CommunicationRobotController();
        int[] multiWellInArray = new int[] {
                0, 0, 0, 0, 0, 0, 0, 0,
                2, 2, 2, 0, 0, 0, 1, 2,
                0, 0, 1, 1, 2, 1, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        rc.setSharedArray(multiWellInArray);
        MapLocation closestWell = Communication.readWell(rc);
        // Assert that closest location is correct.
        assertEquals(new MapLocation(1, 1), closestWell);
        rc.reset();
    }

    // Testing writing a well to an empty shared array when it can write.
    @Test
    public void testWriteWellsToEmptyArrayCanWrite() throws GameActionException {
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
                1, 1, 1, 2, 2, 2, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0
        };

        rc.setCanWriteResult(true);
        Communication.writeWells(rc);
        // Assert that written HQ is equal to validArray.
        assertArrayEquals(validArray, rc.getArray());
        rc.reset();
    }

    @Test
    public void testReadIsland() {
    }

    @Test
    public void testWriteIslands() {
    }

    @Test
    public void testUpdateIslands() {
    }

    @Test
    public void testReadPriority() {
    }

    @Test
    public void testWritePriority() {
    }
}

/**
 * Implements a simple mock RobotController for testing. Has to implement all methods, but the only affected methods
 * are getMapWidth, getMapHeight, getLocation, and senseNearbyWells. New methods for testing include setSharedArray,
 * setCanWriteResult, setLocation, getArray, setWells, setIslands, and reset.
 **/
class CommunicationRobotController implements RobotController {
    MapLocation currentLocation = new MapLocation(0, 0);
    WellInfo[] currentWells = new WellInfo[] {};
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
    public int getID() {
        return 0;
    }

    @Override
    public Team getTeam() {
        return null;
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
    public int[] senseNearbyIslands() {
        return new int[0];
    }

    @Override
    public MapLocation[] senseNearbyIslandLocations(int idx)  {
        return new MapLocation[0];
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
    public Team senseTeamOccupyingIsland(int islandIdx)  {
        return null;
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
