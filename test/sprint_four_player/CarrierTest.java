package sprint_four_player;

import battlecode.common.*;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class CarrierTest {

    // Tests every run through of a Carrier's capabilities.
    @Test
    public void testRunCarrier() throws GameActionException {
        testCanBuildElixirWellNot();
        testCanBuildElixirWell();

        testDepositResourceAmountZero();
        testDepositResourceCannot();
        testDepositResourceCan();

        testCollectFromAnywhere();
        testCollectFromWell();
        testCollectFromWellCannot();

        testCollectAnchorSTANDARD();
        testCollectAnchorACCELERATING();
        testCollectAnchorCannot();
    }

    // Testing cannot build Elixir well.
    @Test
    public void testCanBuildElixirWellNot() {
        CarrierRobotController rc = new CarrierRobotController();
        assertFalse(Carrier.canBuildElixirWell(rc, 1));
        assertFalse(Carrier.canBuildElixirWell(rc, 2));
        assertFalse(Carrier.canBuildElixirWell(rc, 3));
    }

    // Testing can build Elixir well.
    @Test
    public void testCanBuildElixirWell() {
        CarrierRobotController rc = new CarrierRobotController();
        rc.setResourceAmount(ResourceType.ADAMANTIUM, 40);
        // Well type 2 represents mana (opposite type)
        assertTrue(Carrier.canBuildElixirWell(rc, 2));
        rc.setResourceAmount(ResourceType.ADAMANTIUM, 0);
        rc.setResourceAmount(ResourceType.MANA, 40);
        // Well type 1 represents adamantium (opposite type)
        assertTrue(Carrier.canBuildElixirWell(rc, 1));
    }

    // Testing deposit resource but amount too low.
    @Test
    public void testDepositResourceAmountZero() throws GameActionException {
        CarrierRobotController rc = new CarrierRobotController();
        rc.setResourceAmount(ResourceType.NO_RESOURCE, 40);
        assertFalse(Carrier.depositResource(rc, new MapLocation(1, 1), ResourceType.ADAMANTIUM, 0));
        assertEquals(rc.getResourceAmount(ResourceType.NO_RESOURCE), 40);
    }

    // Testing deposit resource cannot transfer.
    @Test
    public void testDepositResourceCannot() throws GameActionException {
        CarrierRobotController rc = new CarrierRobotController();
        rc.setCanDepositResource(false);
        rc.setResourceAmount(ResourceType.NO_RESOURCE, 40);
        assertFalse(Carrier.depositResource(rc, new MapLocation(1, 1), ResourceType.ADAMANTIUM, 40));
        assertEquals(rc.getResourceAmount(ResourceType.NO_RESOURCE), 40);
    }

    // Testing deposit resource can transfer.
    @Test
    public void testDepositResourceCan() throws GameActionException {
        CarrierRobotController rc = new CarrierRobotController();
        rc.setResourceAmount(ResourceType.NO_RESOURCE, 40);
        assertTrue(Carrier.depositResource(rc, new MapLocation(1, 1), ResourceType.ADAMANTIUM, 40));
        assertEquals(rc.getResourceAmount(ResourceType.NO_RESOURCE), 0);
    }

    // Testing collecting from anywhere (just uses collectFromWell method on a loop)
    @Test
    public void testCollectFromAnywhere() throws GameActionException {
        CarrierRobotController rc = new CarrierRobotController();
        rc.setLocation(new MapLocation(1, 1));
        Carrier.collectFromAnywhere(rc, rc.getLocation());
        // Collecting from all direction and current location (9 * 40)
        assertEquals(rc.getResourceAmount(ResourceType.NO_RESOURCE), 360);
    }

    // Testing can collect from well.
    @Test
    public void testCollectFromWell() throws GameActionException {
        CarrierRobotController rc = new CarrierRobotController();
        Carrier.collectFromWell(rc, new MapLocation(0, 0), 1);
        assertEquals(rc.getResourceAmount(ResourceType.NO_RESOURCE), 1);
    }

    // Testing cannot collect from well.
    @Test
    public void testCollectFromWellCannot() throws GameActionException {
        CarrierRobotController rc = new CarrierRobotController();
        rc.setCanCollectResource(false);
        Carrier.collectFromWell(rc, new MapLocation(0, 0), 1);
        assertEquals(rc.getResourceAmount(ResourceType.NO_RESOURCE), 0);
    }

    // Testing can collect STANDARD anchor.
    @Test
    public void testCollectAnchorSTANDARD() throws GameActionException {
        CarrierRobotController rc = new CarrierRobotController();
        rc.setCanTakeAnchor(Anchor.STANDARD);
        Carrier.collectAnchor(rc, new MapLocation(0, 0));
        assertEquals(Anchor.STANDARD, rc.getAnchor());
    }

    // Testing can collect ACCELERATING anchor.
    @Test
    public void testCollectAnchorACCELERATING() throws GameActionException {
        CarrierRobotController rc = new CarrierRobotController();
        rc.setCanTakeAnchor(Anchor.ACCELERATING);
        Carrier.collectAnchor(rc, new MapLocation(0, 0));
        assertEquals(Anchor.ACCELERATING, rc.getAnchor());
    }

    // Testing cannot collect anchor.
    @Test
    public void testCollectAnchorCannot() throws GameActionException {
        CarrierRobotController rc = new CarrierRobotController();
        rc.setCanTakeAnchor(false);
        Carrier.collectAnchor(rc, new MapLocation(0, 0));
        assertNull(rc.getAnchor());
    }
}

 /**
 * Implements a simple mock RobotController for testing Carrier related functionality.
 **/
class CarrierRobotController implements RobotController {
    private boolean canTakeAnchorResult = true;
    private boolean canCollectResourceResult = true;
    private boolean canDepositResourceResult = true;
    private int resourceAmount = 0;
    private int adamantiumAmount = 0;
    private int manaAmount = 0;
    private int elixirAmount = 0;
    private Anchor anchorType;
    private MapLocation currentLocation;

    public void setLocation(MapLocation location) {
        currentLocation = location;
    }

     public void setCanCollectResource(boolean result) {
         canCollectResourceResult = result;
     }

     public void setResourceAmount(ResourceType rType, int amount) {
        switch (rType) {
            case ADAMANTIUM:
                adamantiumAmount = amount;
            case MANA:
                manaAmount = amount;
            case ELIXIR:
                elixirAmount = amount;
            default:
                resourceAmount = amount;
        }
     }

     public void setCanDepositResource(boolean result) {
         canDepositResourceResult = result;
     }

     public void setCanTakeAnchor(boolean result) {
        canTakeAnchorResult = result;
    }

    public void setCanTakeAnchor(Anchor type) {
        anchorType = type;
    }

    @Override
    public int getRoundNum() {
        return 0;
    }

    @Override
    public int getMapWidth() {
        return 0;
    }

    @Override
    public int getMapHeight() {
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
    public MapLocation getLocation() {
        return currentLocation;
    }

    @Override
    public int getHealth() {
        return 0;
    }

    @Override
    public int getResourceAmount(ResourceType rType) {
        switch (rType) {
            case ADAMANTIUM:
                return adamantiumAmount;
            case MANA:
                return manaAmount;
            case ELIXIR:
                return elixirAmount;
            default:
                return resourceAmount;
        }
    }

    @Override
    public Anchor getAnchor() throws GameActionException {
        return anchorType;
    }

    @Override
    public int getNumAnchors(Anchor anchorType) {
        return 0;
    }

    @Override
    public int getWeight() {
        return 40;
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
    public boolean isLocationOccupied(MapLocation loc) throws GameActionException {
        return false;
    }

    @Override
    public boolean canSenseRobotAtLocation(MapLocation loc) {
        return false;
    }

    @Override
    public RobotInfo senseRobotAtLocation(MapLocation loc) throws GameActionException {
        return null;
    }

    @Override
    public boolean canSenseRobot(int id) {
        return false;
    }

    @Override
    public RobotInfo senseRobot(int id) throws GameActionException {
        return null;
    }

    @Override
    public RobotInfo[] senseNearbyRobots() {
        return new RobotInfo[0];
    }

    @Override
    public RobotInfo[] senseNearbyRobots(int radiusSquared) throws GameActionException {
        return new RobotInfo[0];
    }

    @Override
    public RobotInfo[] senseNearbyRobots(int radiusSquared, Team team) throws GameActionException {
        return new RobotInfo[0];
    }

    @Override
    public RobotInfo[] senseNearbyRobots(MapLocation center, int radiusSquared, Team team) throws GameActionException {
        return new RobotInfo[0];
    }

    @Override
    public boolean sensePassability(MapLocation loc) throws GameActionException {
        return false;
    }

    @Override
    public int senseIsland(MapLocation loc) throws GameActionException {
        return 0;
    }

    @Override
    public int[] senseNearbyIslands() {
        return new int[0];
    }

    @Override
    public MapLocation[] senseNearbyIslandLocations(int idx) throws GameActionException {
        return new MapLocation[0];
    }

    @Override
    public MapLocation[] senseNearbyIslandLocations(int radiusSquared, int idx) throws GameActionException {
        return new MapLocation[0];
    }

    @Override
    public MapLocation[] senseNearbyIslandLocations(MapLocation center, int radiusSquared, int idx) throws GameActionException {
        return new MapLocation[0];
    }

    @Override
    public Team senseTeamOccupyingIsland(int islandIdx) throws GameActionException {
        return null;
    }

    @Override
    public int senseAnchorPlantedHealth(int islandIdx) throws GameActionException {
        return 0;
    }

    @Override
    public Anchor senseAnchor(int islandIdx) throws GameActionException {
        return null;
    }

    @Override
    public boolean senseCloud(MapLocation loc) throws GameActionException {
        return false;
    }

    @Override
    public MapLocation[] senseNearbyCloudLocations() {
        return new MapLocation[0];
    }

    @Override
    public MapLocation[] senseNearbyCloudLocations(int radiusSquared) throws GameActionException {
        return new MapLocation[0];
    }

    @Override
    public MapLocation[] senseNearbyCloudLocations(MapLocation center, int radiusSquared) throws GameActionException {
        return new MapLocation[0];
    }

    @Override
    public WellInfo senseWell(MapLocation loc) throws GameActionException {
        return null;
    }

    @Override
    public WellInfo[] senseNearbyWells() {
        return new WellInfo[0];
    }

    @Override
    public WellInfo[] senseNearbyWells(int radiusSquared) throws GameActionException {
        return new WellInfo[0];
    }

    @Override
    public WellInfo[] senseNearbyWells(MapLocation center, int radiusSquared) throws GameActionException {
        return new WellInfo[0];
    }

    @Override
    public WellInfo[] senseNearbyWells(ResourceType resourceType) {
        return new WellInfo[0];
    }

    @Override
    public WellInfo[] senseNearbyWells(int radiusSquared, ResourceType resourceType) throws GameActionException {
        return new WellInfo[0];
    }

    @Override
    public WellInfo[] senseNearbyWells(MapLocation center, int radiusSquared, ResourceType resourceType) throws GameActionException {
        return new WellInfo[0];
    }

    @Override
    public MapInfo senseMapInfo(MapLocation loc) throws GameActionException {
        return null;
    }

    @Override
    public MapInfo[] senseNearbyMapInfos() {
        return new MapInfo[0];
    }

    @Override
    public MapInfo[] senseNearbyMapInfos(int radiusSquared) throws GameActionException {
        return new MapInfo[0];
    }

    @Override
    public MapInfo[] senseNearbyMapInfos(MapLocation center) throws GameActionException {
        return new MapInfo[0];
    }

    @Override
    public MapInfo[] senseNearbyMapInfos(MapLocation center, int radiusSquared) throws GameActionException {
        return new MapInfo[0];
    }

    @Override
    public MapLocation adjacentLocation(Direction dir) {
        return null;
    }

    @Override
    public MapLocation[] getAllLocationsWithinRadiusSquared(MapLocation center, int radiusSquared) throws GameActionException {
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
    public boolean canMove(Direction dir) {
        return false;
    }

    @Override
    public void move(Direction dir) throws GameActionException {

    }

    @Override
    public boolean canBuildRobot(RobotType type, MapLocation loc) {
        return false;
    }

    @Override
    public void buildRobot(RobotType type, MapLocation loc) throws GameActionException {

    }

    @Override
    public boolean canAttack(MapLocation loc) {
        return false;
    }

    @Override
    public void attack(MapLocation loc) throws GameActionException {

    }

    @Override
    public boolean canBoost() {
        return false;
    }

    @Override
    public void boost() throws GameActionException {

    }

    @Override
    public boolean canDestabilize(MapLocation loc) {
        return false;
    }

    @Override
    public void destabilize(MapLocation loc) throws GameActionException {

    }

    @Override
    public boolean canCollectResource(MapLocation loc, int amount) {
        return canCollectResourceResult;
    }

    @Override
    public void collectResource(MapLocation loc, int amount) throws GameActionException {
         if (amount == -1) {
             // Fill up on resources.
             resourceAmount += getWeight();
         }
         else {
             resourceAmount += amount;
         }
    }

    @Override
    public boolean canTransferResource(MapLocation loc, ResourceType rType, int amount) {
        return canDepositResourceResult;
    }

    @Override
    public void transferResource(MapLocation loc, ResourceType rType, int amount) throws GameActionException {
        switch (rType) {
            case ADAMANTIUM:
                adamantiumAmount -= amount;
            case MANA:
                manaAmount -= amount;
            case ELIXIR:
                elixirAmount -= amount;
            default:
                resourceAmount -= amount;
        }
    }

    @Override
    public boolean canBuildAnchor(Anchor anchor) {
        return false;
    }

    @Override
    public void buildAnchor(Anchor anchor) throws GameActionException {

    }

    @Override
    public boolean canTakeAnchor(MapLocation loc, Anchor anchorType) {
        if (canTakeAnchorResult) {
            return anchorType == this.anchorType;
        }
        return false;
    }

    @Override
    public void takeAnchor(MapLocation loc, Anchor anchorType) throws GameActionException {
        this.anchorType = anchorType;
    }

    @Override
    public boolean canReturnAnchor(MapLocation loc) {
        return false;
    }

    @Override
    public void returnAnchor(MapLocation loc) throws GameActionException {

    }

    @Override
    public boolean canPlaceAnchor() {
        return false;
    }

    @Override
    public void placeAnchor() throws GameActionException {

    }

    @Override
    public int readSharedArray(int index) throws GameActionException {
        return 0;
    }

    @Override
    public boolean canWriteSharedArray(int index, int value) {
        return false;
    }

    @Override
    public void writeSharedArray(int index, int value) throws GameActionException {

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