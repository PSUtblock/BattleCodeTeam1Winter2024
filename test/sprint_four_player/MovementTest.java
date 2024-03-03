package sprint_four_player;

import battlecode.common.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Implements unit testing for the Movement class.
 */
public class MovementTest {

    // Testing moveToLocation method when robot can move in a direction.
    @Test
    public void testMoveToLocationDirCanMove() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        Direction validDir = Direction.NORTH;

        // Check resulting location if it can move.
        rc.setCanMoveResult(true);
        Movement.moveToLocation(rc, validDir);
        assertEquals(new MapLocation(0, 1), rc.getLocation());
    }

    // Testing moveToLocation method when robot cannot move in a direction.
    @Test
    public void testMoveToLocationDirCannotMove() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        Direction validDir = Direction.NORTH;

        // Check resulting location if it cannot move.
        rc.setCanMoveResult(false);
        Movement.moveToLocation(rc, validDir);
        assertNotEquals(new MapLocation(0, 1), rc.getLocation());
    }

    // Testing moveToLocation method when a robot's location is equal to the target
    // location.
    @Test
    public void testMoveToLocationTargetSameLocation() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation sameLocation = rc.getLocation();
        rc.setMovementReady(true);
        Movement.moveToLocation(rc, sameLocation);
        assertEquals(new MapLocation(0, 0), rc.getLocation());
    }

    // Testing moveToLocation method when a robot's location is not equal to the target
    // location and movement is not ready.
    @Test
    public void testMoveToLocationTargetMovementNotReady() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation targetLocation = new MapLocation(1, 1);
        rc.setMovementReady(false);
        Movement.moveToLocation(rc, targetLocation);
        assertEquals(new MapLocation(0, 0), rc.getLocation());
    }

    // Testing moveToLocation method to a target location when a robot can move and is adjacent.
    @Test
    public void testMoveToLocationTargetCanMoveIsAdjacent() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation targetLocation = new MapLocation(2, 2);
        rc.setMovementReady(true);
        rc.setCanMoveResult(true);
        Movement.moveToLocation(rc, targetLocation);
        assertEquals(new MapLocation(1, 1), rc.getLocation());
    }

    // Testing moveToLocation method to a target location when a robot can move and is not adjacent.
    @Test
    public void testMoveToLocationTargetCanMoveNotAdjacent() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation targetLocation = new MapLocation(3, 3);
        rc.setMovementReady(true);
        rc.setCanMoveResult(true);
        Movement.moveToLocation(rc, targetLocation);
        assertEquals(new MapLocation(1, 1), rc.getLocation());
    }

    // Testing moveToLocation method to a target location when a robot can move but has already visited it, which means
    // it can move clockwise.
    @Test
    public void testMoveToLocationTargetCanMoveButAlreadyVisited() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        Movement.moveToLocation(rc, new MapLocation(5, 5));
        Movement.moveToLocation(rc, new MapLocation(5, 5));
        Movement.moveToLocation(rc, new MapLocation(5, 5));
        rc.setMovementReady(true);
        rc.setCanMoveResult(true);
        assertTrue(Movement.movedClockwise(rc, Direction.SOUTHWEST, rc.getLocation())); // Southwest is a filler
    }

    // Testing moveToLocation method to a target location when a robot can move but has already visited it and clockwise
    // fails.
    @Test
    public void testMoveToLocationTargetCanMoveButAlreadyVisitedAndCannotMoveClockwise() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        Movement.moveToLocation(rc, new MapLocation(0, 10));
        Movement.moveToLocation(rc, new MapLocation(0, 10));
        Movement.moveToLocation(rc, new MapLocation(10, 2));
        Movement.moveToLocation(rc, new MapLocation(10, 2));
        Movement.moveToLocation(rc, new MapLocation(2, -10));
        Movement.moveToLocation(rc, new MapLocation(2, -10));
        Movement.moveToLocation(rc, new MapLocation(-10, 0));
        Movement.moveToLocation(rc, new MapLocation(1, 10));
        rc.setMovementReady(true);
        rc.setCanMoveResult(true);
        assertFalse(Movement.movedClockwise(rc, Direction.NORTHEAST, rc.getLocation()));
    }

    // Testing moveToLocation method to a target location when a robot cannot move.
    @Test
    public void testMoveToLocationTargetCannotMove() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        MapLocation targetLocation = new MapLocation(1, 1);
        rc.setMovementReady(true);
        rc.setCanMoveResult(false);
        Movement.moveToLocation(rc, targetLocation);
        assertEquals(new MapLocation(0, 0), rc.getLocation());
    }

    // Testing movedClockwise method to a target location when a robot can move.
    @Test
    public void testMoveClockwiseCanMove() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        Direction validDir = Direction.NORTHEAST;
        rc.setCanMoveResult(true);
        assertTrue(Movement.movedClockwise(rc, validDir, rc.getLocation()));
    }

    // Testing movedClockwise method to a target location when a robot can move but has already visited a location.
    @Test
    public void testMoveClockwiseCanMoveButAlreadyVisitedALocation() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        Movement.moveToLocation(rc, new MapLocation(0, 10));
        Movement.moveToLocation(rc, new MapLocation(0, 10));
        Movement.moveToLocation(rc, new MapLocation(10, 2));
        Movement.moveToLocation(rc, new MapLocation(10, 2));
        Movement.moveToLocation(rc, new MapLocation(2, -10));
        Movement.moveToLocation(rc, new MapLocation(2, -10));
        rc.setCanMoveResult(true);
        assertTrue(Movement.movedClockwise(rc, Direction.NORTH, rc.getLocation()));
    }

    // Testing movedClockwise method to a target location when a robot can move but has already visited every location.
    @Test
    public void testMovedClockwiseCanMoveButAlreadyVisitedEverything() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        Movement.moveToLocation(rc, new MapLocation(0, 10));
        Movement.moveToLocation(rc, new MapLocation(0, 10));
        Movement.moveToLocation(rc, new MapLocation(10, 2));
        Movement.moveToLocation(rc, new MapLocation(10, 2));
        Movement.moveToLocation(rc, new MapLocation(2, -10));
        Movement.moveToLocation(rc, new MapLocation(2, -10));
        Movement.moveToLocation(rc, new MapLocation(-10, 0));
        Movement.moveToLocation(rc, new MapLocation(1, 10));
        rc.setCanMoveResult(true);
        assertFalse(Movement.movedClockwise(rc, Direction.NORTH, rc.getLocation()));
        assertEquals(new MapLocation(1, 1), rc.getLocation());
    }

    // Testing moveClockwise method to a target location when a robot cannot move.
    @Test
    public void testMoveClockwiseCannotMove() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        Direction validDir = Direction.NORTHEAST;
        rc.setCanMoveResult(false);
        assertFalse(Movement.movedClockwise(rc, validDir, rc.getLocation()));
        assertEquals(new MapLocation(0, 0), rc.getLocation());
    }

    // Testing moveRandomly if can move.
    @Test
    public void testMoveRandomlyCanMove() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        rc.setCanMoveResult(true);
        Movement.moveRandomly(rc);
        // Nothing to check.
    }

    // Testing moveRandomly if cannot move.
    @Test
    public void testMoveRandomlyCannotMove() throws GameActionException {
        MovementRobotController rc = new MovementRobotController();
        rc.setCanMoveResult(false);
        Movement.moveRandomly(rc);
        assertEquals(new MapLocation(0, 0), rc.getLocation());
    }

    // Testing explore if target not locked.
    @Test
    public void testExploreNotLocked() throws GameActionException {
        MappingRobotController rc = new MappingRobotController();
        resetVisitLandmarks(rc);
        rc.setLocation(new MapLocation(5, 5));
        Movement.explore(rc);
        assertEquals(new MapLocation(6, 6), rc.getLocation());
    }

    // Helper function for testing exploration.
    private void resetVisitLandmarks(MappingRobotController rc) throws GameActionException {
        int count = 0;
        boolean visitedOne = false;
        boolean visitedTwo = false;
        boolean visitedThree = false;
        boolean visitedFour = false;
        while (count < 4) {
            Movement.explore(rc);
            if (!visitedOne && rc.getLocation().isAdjacentTo(new MapLocation(0,0))) {
                ++count;
                visitedOne = true;
            }
            if (!visitedTwo && rc.getLocation().isAdjacentTo(new MapLocation(0, 9))) {
                ++count;
                visitedTwo = true;
            }
            if (!visitedThree && rc.getLocation().isAdjacentTo(new MapLocation(9, 0))) {
                ++count;
                visitedThree = true;
            }
            if (!visitedFour && rc.getLocation().isAdjacentTo(new MapLocation(9, 9))) {
                ++count;
                visitedFour = true;
            }
        }
    }

    // Testing explore if location to explore is null.
    @Test
    public void testExploreLocationToExploreNull() throws GameActionException {
        MappingRobotController rc = new MappingRobotController();
        rc.setMapWidthAndHeight(-1, -1);
        rc.setLocation(new MapLocation(5, 5));
        Movement.explore(rc);
        assertEquals(new MapLocation(5, 5), rc.getLocation());
    }

    // Testing explore if adjacent to location.
    @Test
    public void testExploreAdjacentToLocation() throws GameActionException {
        MappingRobotController rc = new MappingRobotController();
        rc.setLocation(new MapLocation(5, 5));
        Movement.explore(rc); // to (6, 6)
        assertEquals(new MapLocation(6, 6), rc.getLocation());
        Movement.explore(rc); // to (7, 7)
        assertEquals(new MapLocation(7, 7), rc.getLocation());
        Movement.explore(rc); // (8, 8)
        assertEquals(new MapLocation(8, 8), rc.getLocation());
        Movement.explore(rc); // now adjacent
        assertEquals(new MapLocation(8, 8), rc.getLocation());
    }

    // Testing explore if visited all.
    @Test
    public void testExploreVisitedAll() throws GameActionException {
        MappingRobotController rc = new MappingRobotController();
        rc.setLocation(new MapLocation(5, 5));
        resetVisitLandmarks(rc); //Visits all locations
        assertEquals(new MapLocation(1, 0), rc.getLocation());
    }

}


/**
 * Implements a simple mock RobotController for testing. Has to implement all methods, but the only affected methods
 * are getMapWidth, getMapHeight, getLocation, canMove, move, canWriteSharedArray, setIndicatorString, and
 * isMovementReady. New methods for testing include getIndicatorString, setCanMoveResult, setLocation,
 * setMapWidthAndHeight, and reset.
 **/
class MovementRobotController implements RobotController{
    private boolean canMoveResult = true; // Controls canMove result
    private boolean movementReadyResult = true; // Controls isMovementReady result
    private String indicator = "";
    private MapLocation currentLocation = new MapLocation(0, 0);
    private int mapWidth = 11;
    private int mapHeight = 11;

    public void setCanMoveResult(boolean moveResult) {
        canMoveResult = moveResult;
    }

    public void setLocation(MapLocation location) {
        currentLocation = location;
    }

    public String getIndicatorString() {
        return indicator;
    }

    public void setMovementReady(boolean movementReady) {
        movementReadyResult = movementReady;
    }

    @Override
    public boolean isMovementReady() {
        return movementReadyResult;
    }

    @Override
    public boolean canWriteSharedArray(int index, int value) {
        return true;
    }

    @Override
    public void setIndicatorString(String string) {
        indicator = string;
    }

    @Override
    public int getMapWidth() {
        return mapWidth;
    }

    @Override
    public int getMapHeight() {
        return mapHeight;
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
    public void writeSharedArray(int index, int value)  {

    }

    @Override
    public void disintegrate() {

    }

    @Override
    public void resign() {

    }

    @Override
    public void setIndicatorDot(MapLocation loc, int red, int green, int blue) {

    }

    @Override
    public void setIndicatorLine(MapLocation startLoc, MapLocation endLoc, int red, int green, int blue) {

    }
}