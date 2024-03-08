package sprint_four_player;

import battlecode.common.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class PackingTest {
    // Test combining x and y coordinates into one value.
    @Test
    public void testPackCoordinatesIntoValue() throws GameActionException {
        PackingRobotController rc = new PackingRobotController();
        MapLocation location = new MapLocation(1, 2);
        assertEquals(8, Packing.packCoordinates(rc, location));
    }

    // Test getting the x and y coordinate from one value.
    @Test
    public void testUnpackCoordinatesFromValue() throws GameActionException {
        PackingRobotController rc = new PackingRobotController();
        int mapValue = 8;
        assertEquals(new MapLocation(1, 2), Packing.unpackCoordinates(rc, mapValue));
    }

    // Test packing Object information with no type into one value.
    @Test
    public void testPackObjectIntoValueNoType() throws GameActionException {
        PackingRobotController rc = new PackingRobotController();
        MapLocation locationToPack = new MapLocation(2, 2);
        int packedValue = 144;
        assertEquals(packedValue, Packing.packObject(rc, locationToPack));
    }

    // Test packing Headquarters information into one value.
    @Test
    public void testPackObjectIntoValue() throws GameActionException {
        PackingRobotController rc = new PackingRobotController();
        MapLocation locationToPack = new MapLocation(2, 2);
        int typeToPack = 2;
        int packedValue = 146;
        int testValue = Packing.packObject(rc, locationToPack, typeToPack);
        assertEquals(packedValue, testValue);
    }

    // Test unpacking Headquarters information from one value.
    @Test
    public void testUnpackObjectFromValue() throws GameActionException {
        PackingRobotController rc = new PackingRobotController();
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
        PackingRobotController rc = new PackingRobotController();
        assertEquals(0, Packing.getIslandStateNum(rc.getTeam(), Team.NEUTRAL));
    }

    // Test reading if an island is occupied by team.
    @Test
    public void testReadTeamOccupiedIsland() {
        PackingRobotController rc = new PackingRobotController();
        assertEquals(1, Packing.getIslandStateNum(rc.getTeam(), Team.A));
    }

    // Test reading if an island is occupied by opponent.
    @Test
    public void testReadOpponentOccupiedIsland() {
        PackingRobotController rc = new PackingRobotController();
        assertEquals(2, Packing.getIslandStateNum(rc.getTeam(), Team.B));
    }
}

/**
 * Implements a simple mock RobotController for testing Packing related functionality.
 **/
class PackingRobotController implements RobotController {
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

    public void setLocation(MapLocation location) {
        currentLocation = location;
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
