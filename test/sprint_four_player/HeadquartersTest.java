package sprint_four_player;

import battlecode.common.*;
import org.junit.Test;
import org.scalactic.Or;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static sprint_four_player.RobotPlayer.directions;
import static sprint_four_player.RobotPlayer.rng;

public class HeadquartersTest {

    public HeadquartersRobotController rc = new HeadquartersRobotController();

    // Testing writing location to shared array if not round 1.
    @Test
    public void testWriteHQLocationNotRoundOne() throws GameActionException {
        HeadquartersRobotController rc = new HeadquartersRobotController();
        rc.setRoundNum(2);
        rc.setLocation(new MapLocation(1, 1));
        sprint_four_player.Headquarters.runHeadquarters(rc);
        assertNull(sprint_four_player.Communication.readHQ(rc));
    }

    // Testing writing location to shared array if round 1.
    @Test
    public void testWriteHQLocationRoundOne() throws GameActionException {
        HeadquartersRobotController rc = new HeadquartersRobotController();
        rc.setRoundNum(1);
        rc.setLocation(new MapLocation(1, 1));
        Headquarters.runHeadquarters(rc);
        assertEquals(new MapLocation(1, 1), Communication.readHQ(rc));
    }
    @Test
    public void testSpawnAnchor()throws GameActionException{
        Headquarters.spawnAnchor(rc);
        assertNull(rc.getAnchor());
    }
    @Test
    public void testSpawnAcceleratingAnchor() throws GameActionException{
        Headquarters.spawnAcceleratingAnchor(rc);
        assertNull(rc.getAnchor());
    }

    @Test
    public void testSpawnCarriers()throws GameActionException{
        Direction dir = directions[rng.nextInt(directions.length)];
        MapLocation newLoc = rc.getLocation().add(dir);
        Headquarters.spawnCarriers(rc, newLoc);
    }
    @Test
    public void testSpawnLaunchers()throws GameActionException{
        Direction dir = directions[rng.nextInt(directions.length)];
        MapLocation newLoc = rc.getLocation().add(dir);
        Headquarters.spawnLauncher(rc, newLoc);
    }
    @Test
    public void testSpawnAmplifier() throws GameActionException{
        Direction dir = directions[rng.nextInt(directions.length)];
        MapLocation newLoc = rc.getLocation().add(dir);
        Headquarters.spawnAmplifier(rc, newLoc);
    }
    @Test
    public void testSpawnTemporalBoosters() throws GameActionException{
        Direction dir = directions[rng.nextInt(directions.length)];
        MapLocation newLoc = rc.getLocation().add(dir);
        Headquarters.spawnTemporalBoosters(rc, newLoc);
    }
    @Test
    public void testSpawnDestabilizer()throws GameActionException{
        Direction dir = directions[rng.nextInt(directions.length)];
        MapLocation newLoc = rc.getLocation().add(dir);
        Headquarters.spawnDestabilizer(rc, newLoc);
    }

}

class HeadquartersRobotController implements RobotController {
    MapLocation currentLocation = new MapLocation(0, 0);
    int roundNum = 0;
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

    public void setRoundNum(int num) {
        roundNum = num;
    }

    public void setLocation(MapLocation newLocation) {
        currentLocation = newLocation;
    }

    @Override
    public int readSharedArray(int index) throws GameActionException {
        return sharedArray[index];
    }

    @Override
    public boolean canWriteSharedArray(int index, int value) {
        return canWriteResult;
    }

    @Override
    public void writeSharedArray(int index, int value) throws GameActionException {
        sharedArray[index] = value;
    }

    @Override
    public MapLocation getLocation() {
        return currentLocation;
    }

    @Override
    public int getRoundNum() {
        return roundNum;
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
    public int getNumAnchors(Anchor anchorType) {
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
    public Anchor getAnchor() throws GameActionException {
        return null;
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