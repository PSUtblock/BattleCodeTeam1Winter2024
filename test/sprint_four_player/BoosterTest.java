package sprint_four_player;

import battlecode.common.*;
import battlecode.world.Inventory;
import org.junit.Test;


import static org.junit.Assert.*;

public class BoosterTest {

    // Tests every run through of a Carrier's capabilities.
    @Test
    public void testRunBooster() throws GameActionException {
        //test the act of running the run
        testRunTemporalBooster();
        //testing findfriend method.
        testFindFriendCarrier();
        testFindFriendLauncher();
        testFindFriendLauncherCannotFind();
        testFindFriendCarrierCannotFind();
        testFindFriendLoner();
        testFindFriendRandom();
        //testing boosting function
        testcanBoost();
        testcannotBoost();
        //testing boost friend area
        testboostFriendsensed();


    }

    @Test
    public void testRunTemporalBooster() throws GameActionException {
        BoosterRobotController rc = new BoosterRobotController();
        TemporalBoosters.runTemporalBoosters(rc);
        assertTrue(true);
    }

    @Test
    public void testFindFriendCarrier() {
        BoosterRobotController rc = new BoosterRobotController();
        TemporalBoosters.setRoboBuddID(-1);
        rc.setTeam(Team.A);
        int friend = TemporalBoosters.findFriend(rc,1);
        assertEquals(1, friend);
    }

    @Test
    public void testFindFriendRandom() {
        BoosterRobotController rc = new BoosterRobotController();
        TemporalBoosters.setRoboBuddID(-1);
        rc.setTeam(Team.A);
        int friend = TemporalBoosters.findFriend(rc,44);
        assertEquals(0, friend);
    }
    @Test
    public void testFindFriendLauncher() {
        BoosterRobotController rc = new BoosterRobotController();
        TemporalBoosters.setRoboBuddID(-1);
        rc.setTeam(Team.A);
        int friend = TemporalBoosters.findFriend(rc,2);
        assertEquals(2,friend);
    }

    @Test
    public void testFindFriendLauncherCannotFind(){
        BoosterRobotController rc = new BoosterRobotController();
        TemporalBoosters.setRoboBuddID(-1);
        rc.setTeam(Team.B);
        int friend = TemporalBoosters.findFriend(rc,2);
        assertEquals(0, friend);
    }

    @Test
    public void testFindFriendCarrierCannotFind(){
        BoosterRobotController rc = new BoosterRobotController();
        TemporalBoosters.setRoboBuddID(-1);
        rc.setTeam(Team.B);
        int friend = TemporalBoosters.findFriend(rc,1);
        assertEquals(0, friend);
    }

    @Test
    public void testFindFriendLoner() {
        BoosterRobotController rc = new BoosterRobotController();
        TemporalBoosters.setRoboBuddID(-1);
        int friend = TemporalBoosters.findFriend(rc,3);
        assertEquals(-1, friend);
    }

    @Test
    public void testcanBoost() throws GameActionException {
        BoosterRobotController rc = new BoosterRobotController();
        boolean result = TemporalBoosters.boostArea(rc);
        assertTrue(result);
    }

    @Test
    public void testcannotBoost() throws GameActionException {
        BoosterRobotController rc = new BoosterRobotController();
        rc.setCanBoost(false);
        boolean result = TemporalBoosters.boostArea(rc);
        assertFalse(result);
    }

    @Test
    public void testboostFriendsensed() throws GameActionException {
        BoosterRobotController rc = new BoosterRobotController();
        TemporalBoosters.boostFriendArea(rc,1);
        TemporalBoosters.boostFriendArea(rc,1);
        TemporalBoosters.boostFriendArea(rc, 3);
        assertTrue(true);
    }



}

class BoosterRobotController implements RobotController {

    public MapLocation friendLocation = new MapLocation(2, 2);
    Team myteam = Team.A;
    boolean canBoost = true;
    RobotInfo[] friendlies = new RobotInfo[]{
            new RobotInfo(0, Team.A, RobotType.CARRIER, new Inventory(), 150, friendLocation),
            new RobotInfo(1, Team.A, RobotType.LAUNCHER, new Inventory(), 150, friendLocation),
            new RobotInfo(2, Team.A, RobotType.AMPLIFIER, new Inventory(), 150, friendLocation),
            new RobotInfo(3, Team.A, RobotType.LAUNCHER, new Inventory(), 150, friendLocation),
            new RobotInfo(4, Team.A, RobotType.CARRIER, new Inventory(0,0,0,0,0,0), 150, friendLocation)
    };


    public void setTeam(Team team) {
        myteam = team;
    }

    public void setCanBoost(boolean choice) {
        canBoost = choice;
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
        return myteam;
    }

    @Override
    public RobotType getType() {
        return null;
    }

    @Override
    public MapLocation getLocation() {
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
    public Anchor getAnchor() throws GameActionException {
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
        return id == -1;
    }

    @Override
    public RobotInfo senseRobot(int id) throws GameActionException {
        return friendlies[0];
    }

    @Override
    public RobotInfo[] senseNearbyRobots() {
        return friendlies;
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
        return canBoost;
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
        return false;
    }

    @Override
    public void collectResource(MapLocation loc, int amount) throws GameActionException {

    }

    @Override
    public boolean canTransferResource(MapLocation loc, ResourceType rType, int amount) {
        return false;
    }

    @Override
    public void transferResource(MapLocation loc, ResourceType rType, int amount) throws GameActionException {

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
        return false;
    }

    @Override
    public void takeAnchor(MapLocation loc, Anchor anchorType) throws GameActionException {

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
