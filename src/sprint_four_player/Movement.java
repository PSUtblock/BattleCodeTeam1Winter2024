package sprint_four_player;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.*;

import static sprint_four_player.RobotPlayer.directions;
import static sprint_four_player.RobotPlayer.rng;

/**
 * Unify movement capabilities into one class.
 */
public class Movement {
    private static final int NUM_DIRS = 8;                                   // Total number of directions not including CENTER.
    private static Direction currentDir = null;                              // Holds the direction robot is facing.
    private static Set<MapLocation> visitedLocations = new HashSet<>();      // Reduces location repeats.
    private static List<MapLocation> potentialLandmarks = new ArrayList<>(); // Holds potential landmarks to explore.
    private static Set<MapLocation> visitedLandmarks = new HashSet<>();      // Reduces landmark repeats.
    private static MapLocation locToExplore = null;                          // Holds a location to explore.
    private static boolean targetLocked = false;                             // Locks onto a location to explore.

    /** Move the robot in a given direction. **/
    public static void moveToLocation(RobotController rc, Direction dir) throws GameActionException {
        // If the robot can move, move in that direction. Otherwise, try to move randomly.
        if (rc.canMove(dir)) {
            rc.move(dir);
        }
    }

    /** Move the robot to a given target. **/
    public static void moveToLocation(RobotController rc, MapLocation target) throws GameActionException {
        MapLocation currentLoc = rc.getLocation();
        visitedLocations.add(currentLoc);

        // If at target or cannot move, get out of method.
        if (currentLoc.equals(target) || !rc.isMovementReady()) {
            return;
        }

        try {
            Direction dir = rc.getLocation().directionTo(target);
            MapLocation nextLocation = currentLoc.add(dir);
            if (rc.canMove(dir)) {
                // If next location is adjacent to target, clear the visited log.
                if (nextLocation.isAdjacentTo(target)) {
                    visitedLocations.clear();
                }
                // If the location has not been visited, move to it.
                if (!visitedLocations.contains(nextLocation)) {
                    rc.move(dir);
                    return;
                }
            }

            // Otherwise, there is an obstacle we must go around (clockwise).
            if (movedClockwise(rc, dir, currentLoc)) {
                return;
            }

            // If robot still cannot move, move randomly.
            moveRandomly(rc);
        }
        catch (GameActionException e) {
            rc.setIndicatorString("Cannot Move");
        }
    }

    /** Try to move in a clockwise direction **/
    protected static boolean movedClockwise(RobotController rc, Direction dir, MapLocation currentLoc) throws GameActionException {
        if (currentDir == null) {
            currentDir = dir;
        }
        // For all possible directions, try to move in a clockwise motion around the obstacle.
        for (int i = 0; i < NUM_DIRS; ++i) {
            MapLocation nextLocation = currentLoc.add(currentDir);
            // If no obstacle, move and rotate right to face toward target again.
            if (rc.canMove(currentDir) && !visitedLocations.contains(nextLocation)) {
                rc.move(currentDir);
                currentDir = currentDir.rotateRight();
                return true;
            }
            currentDir = currentDir.rotateLeft();
        }
        return false;
    }

    /** Move randomly **/
    protected static void moveRandomly(RobotController rc) throws GameActionException {
        Direction randDir = directions[rng.nextInt(directions.length)];
        if (rc.canMove(randDir)) {
            rc.move(randDir);
            visitedLocations.clear();
            rc.setIndicatorString("Moving Randomly");
        }
    }

    /** Return the closest location with respect to robot's current location. Uses an array of MapLocations as a parameter. **/
    public static MapLocation getClosestLocation(RobotController rc, MapLocation[] locations) {
        if (locations.length > 0) {
            MapLocation currClosest = locations[0];
            MapLocation myLocation = rc.getLocation();
            int minDistance = myLocation.distanceSquaredTo(currClosest);

            for (MapLocation island : locations) {
                int currDistance = myLocation.distanceSquaredTo(island);
                if (minDistance > currDistance) {
                    minDistance = currDistance;
                    currClosest = island;
                }
            }
            return currClosest;
        }

        return null;
    }

    /** Return the closest location with respect to robot's current location. Uses set of MapLocations as a parameter. **/
    public static MapLocation getClosestLocation(RobotController rc, Set<MapLocation> locations) {
        if (!locations.isEmpty()) {
            MapLocation currClosest = locations.iterator().next();
            MapLocation myLocation = rc.getLocation();
            int minDistance = myLocation.distanceSquaredTo(currClosest);

            for (MapLocation island : locations) {
                int currDistance = myLocation.distanceSquaredTo(island);
                if (minDistance > currDistance) {
                    minDistance = currDistance;
                    currClosest = island;
                }
            }
            return currClosest;
        }
        return null;
    }

    /** Explore randomly with a general direction toward center of map **/
    public static void explore(RobotController rc) throws GameActionException {
        int mapWidth = rc.getMapWidth();
        int mapHeight = rc.getMapHeight();
        int radius = 10; // Landmarks are typically within 100 units of each other.

        // Fill potential landmarks.
        if (potentialLandmarks.isEmpty()) {
            getPossibleLandmarks(rc, mapWidth, mapHeight, radius);
        }

        // If potential landmark not locked find one not already visited.
        if (!targetLocked) {
            for (MapLocation landmark : potentialLandmarks) {
                if (!visitedLandmarks.contains(landmark)) {
                    locToExplore = landmark;
                    targetLocked = true;
                    break;
                }
            }
        }

        if (locToExplore != null) {
            // If near the target, stop travelling to it and add to visit.
            if (rc.getLocation().isAdjacentTo(locToExplore)) {
                visitedLandmarks.add(locToExplore);
                targetLocked = false;
                // If all locations have been added, start over.
                if (visitedLandmarks.size() == potentialLandmarks.size()) {
                    visitedLandmarks.clear();
                }
            }
            else {
                // Otherwise, move to the location.
                moveToLocation(rc, locToExplore);
            }
        }
    }

    /** Fills a list with possible landmarks spaced out by 100 units **/
    private static void getPossibleLandmarks(RobotController rc, int mapWidth, int mapHeight, int radius) throws GameActionException {
        for (int x = 0; x < mapWidth; x += radius - 1) {
            for (int y = 0; y < mapHeight; y += radius - 1) {
                MapLocation potentialLocation = new MapLocation(x, y);
                potentialLandmarks.add(potentialLocation);
            }
        }
        sortLocations(rc, potentialLandmarks);
    }

    /** Sorts locations with respect to distance from a headquarters or from self if necessary **/
    private static void sortLocations(RobotController rc, List<MapLocation> locations) throws GameActionException {
        MapLocation centerLoc = Communication.readHQ(rc);
        if (centerLoc == null) {
            centerLoc = rc.getLocation();
        }
        locations.sort(new DistanceComparator(centerLoc));
    }
}

class DistanceComparator implements Comparator<MapLocation> {
    private final MapLocation targetLocation;

    public DistanceComparator(MapLocation targetLocation) {
        this.targetLocation = targetLocation;
    }

    @Override
    public int compare(MapLocation location1, MapLocation location2) {
        // Compare based on distanceSquaredTo the target location
        int distance1 = location1.distanceSquaredTo(targetLocation);
        int distance2 = location2.distanceSquaredTo(targetLocation);

        // Use Integer.compare for natural order (smallest to largest)
        return Integer.compare(distance1, distance2);
    }
}
