package sprint_three_player;

import battlecode.common.*;
import org.junit.Test;
import sprint_three_player.Movement;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Implements unit testing for the Movement class.
 */
public class MovementTest {

    // Testing moveToLocation method when robot can move in a direction.
    @Test
    public void testMoveToLocationDirCanMove() throws GameActionException{
        MovementRobotController rc = new MovementRobotController();
        Direction validDir = Direction.NORTH;

        // Check resulting location if it can move.
        rc.setCanMoveResult(true);
        sprint_three_player.Movement.moveToLocation(rc, validDir);
        assertEquals(new MapLocation(0, 1), rc.getLocation());
        rc.reset();
    }

    // Testing moveToLocation method when robot cannot move in a direction.
    @Test
    public void testMoveToLocationDirCannotMove() throws GameActionException{
        MovementRobotController rc = new MovementRobotController();
        Direction validDir = Direction.NORTH;

        // Check resulting location if it cannot move.
        rc.setCanMoveResult(false);
        sprint_three_player.Movement.moveToLocation(rc, validDir);
        assertNotEquals(new MapLocation(0, 1), rc.getLocation());
        rc.reset();
    }

    // Testing moveToLocation method when a robot's location is equal to the target
    // location.
    @Test
    public void testMoveToLocationTargetSameLocation() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation sameLocation = rc.getLocation();
        rc.setMovementReady(false);
        sprint_three_player.Movement.moveToLocation(rc, sameLocation);
        assertEquals(new MapLocation(0, 0), rc.getLocation());
        rc.reset();
    }

    // Testing moveToLocation method when a robot's location is not equal to the target
    // location and movement is not ready.
    @Test
    public void testMoveToLocationTargetMovementNotReady() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation targetLocation = new MapLocation(1, 1);
        rc.setMovementReady(false);
        sprint_three_player.Movement.moveToLocation(rc, targetLocation);
        assertEquals(new MapLocation(0, 0), rc.getLocation());
        rc.reset();
    }

    // Testing moveToLocation method to a target location when a robot can move.
    @Test
    public void testMoveToLocationTargetCanMove() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation targetLocation = new MapLocation(1, 1);
        rc.setMovementReady(true);
        rc.setCanMoveResult(true);
        sprint_three_player.Movement.moveToLocation(rc, targetLocation);
        assertEquals(new MapLocation(1, 1), rc.getLocation());
        rc.reset();
    }

    // Testing moveToLocation method to a target location when a robot cannot move.
    @Test
    public void testMoveToLocationTargetCannotMove() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation targetLocation = new MapLocation(1, 1);
        rc.setMovementReady(true);
        rc.setCanMoveResult(false);
        sprint_three_player.Movement.moveToLocation(rc, targetLocation);
        assertEquals(new MapLocation(0, 0), rc.getLocation());
        rc.reset();
    }

    // Testing moveClockwise method to a target location when a robot can move.
    @Test
    public void testMoveClockwiseCanMove() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation targetLocation = new MapLocation(1, 1);
        Direction resultingDir = null;
        Direction validDir = Direction.NORTHEAST;
        Direction solutionDir = Direction.EAST;
        rc.setCanMoveResult(true);
        resultingDir = sprint_two_player.Movement.moveClockwise(rc, validDir);

        assertEquals(new MapLocation(1, 1), rc.getLocation());
        assertEquals(solutionDir, resultingDir);
        rc.reset();
    }

    // Testing moveClockwise method to a target location when a robot cannot move.
    @Test
    public void testMoveClockwiseCannotMove() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation targetLocation = new MapLocation(1, 1);
        Direction resultingDir = null;
        Direction validDir = Direction.NORTHEAST;
        rc.setCanMoveResult(false);
        resultingDir = sprint_two_player.Movement.moveClockwise(rc, validDir);

        assertEquals(new MapLocation(0, 0), rc.getLocation());
        assertEquals(validDir, resultingDir);
        rc.reset();
    }

    // Testing getClosestLocation method with an array of zero locations.
    @Test
    public void testGetClosestLocationWithEmptyArray() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation[] locations = {};

        // Assert that the returned location is null.
        MapLocation closestLocation = sprint_two_player.Movement.getClosestLocation(rc, locations);
        assertNull(closestLocation);
    }

    // Testing getClosestLocation method with an array of one location.
    @Test
    public void testGetClosestLocationWithSingleLocation() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation[] locations = {
                new MapLocation(1, 1)
        };

        // Assert that closest location is correct.
        MapLocation closestLocation = sprint_two_player.Movement.getClosestLocation(rc, locations);
        assertEquals(new MapLocation(1, 1), closestLocation);
    }

    // Testing getClosestLocation method with an array of multiple locations.
    @Test
    public void testGetClosestLocationWithMultipleLocations() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation[] locations = {
                new MapLocation(2, 2),
                new MapLocation(1, 1),
                new MapLocation(2, 0)
        };

        // Assert that closest location is correct.
        MapLocation closestLocation = sprint_two_player.Movement.getClosestLocation(rc, locations);
        assertEquals(new MapLocation(1, 1), closestLocation);
    }

    // Testing getClosestLocation method with a set of zero locations.
    @Test
    public void testGetClosestLocationWithEmptySet() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        Set<MapLocation> locations = new HashSet<>();

        // Assert that the returned location is null.
        MapLocation closestLocation = sprint_two_player.Movement.getClosestLocation(rc, locations);
        assertNull(closestLocation);
    }

    // Testing getClosestLocation method with an array of one location.
    @Test
    public void testGetClosestLocationWithSingleLocationSet() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        Set<MapLocation> locations = new HashSet<>(Collections.singletonList(new MapLocation(1, 1)));

        // Assert that closest location is correct.
        MapLocation closestLocation = sprint_two_player.Movement.getClosestLocation(rc, locations);
        assertEquals(new MapLocation(1, 1), closestLocation);
    }

    // Testing getClosestLocation method with an array of multiple locations.
    @Test
    public void testGetClosestLocationWithMultipleLocationsSet() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        Set<MapLocation> locations = new HashSet<>(
                Arrays.asList(new MapLocation(2, 2), new MapLocation(1, 1), new MapLocation(2, 0)));

        // Assert that closest location is correct.
        MapLocation closestLocation = Movement.getClosestLocation(rc, locations);
        assertEquals(new MapLocation(1, 1), closestLocation);
    }
}

/**
 * Implements a simple mock RobotController for testing. Has to implement all methods, but the only affected methods
 * are getMapWidth, getMapHeight, getLocation, canMove, move, and isMovementReady. New methods for testing include
 * setCanMoveResult and reset.
 **/
class MovementRobotController implements RobotController{
    private boolean canMoveResult = true; // Controls canMove result
    private boolean movementReadyResult = true; // Controls isMovementReady result
    private MapLocation currentLocation = new MapLocation(0, 0);

    public void setCanMoveResult(boolean moveResult) {
        canMoveResult = moveResult;
    }

    public void reset() {
        canMoveResult = true;
        currentLocation = new MapLocation(0, 0);
    }

    public void setMovementReady(boolean movementReady) {
        movementReadyResult = movementReady;
    }

    @Override
    public boolean isMovementReady() {
        return movementReadyResult;
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
    public boolean canMove(Direction dir) {
        return canMoveResult;
    }

    @Override
    public void move(Direction dir) {
        currentLocation = currentLocation.add(dir);
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
    public WellInfo[] senseNearbyWells() {
        return new WellInfo[0];
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
    public int readSharedArray(int index)  {
        return 0;
    }

    @Override
    public boolean canWriteSharedArray(int index, int value) {
        return false;
    }

    @Override
    public void writeSharedArray(int index, int value)  {

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