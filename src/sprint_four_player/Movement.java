package sprint_four_player;

import battlecode.common.*;
import java.util.*;

import static sprint_four_player.RobotPlayer.directions;
import static sprint_four_player.RobotPlayer.rng;

/**
 * Unify movement capabilities into one class.
 */
public class Movement {
    private static final int NUM_DIRS = 8;                                   // Total number of directions not including CENTER.
    private static final int RADIUS = 10;                                    // Approximate radius between wells.
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
            Direction dir = currentLoc.directionTo(target);
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

    /** Explore the map **/
    public static void explore(RobotController rc) throws GameActionException {
        int mapWidth = rc.getMapWidth();
        int mapHeight = rc.getMapHeight();

        // Fill potential landmarks.
        if (potentialLandmarks.isEmpty()) {
            potentialLandmarks = Mapping.getPossibleLandmarks(rc, mapWidth, mapHeight, RADIUS);
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
            } else {
                // Otherwise, move to the location.
                moveToLocation(rc, locToExplore);
            }
        }
    }

    /** Reset all globals (for testing purposes) **/
    public static void resetGlobals() {
        potentialLandmarks.clear();
        visitedLocations.clear();
        visitedLandmarks.clear();
        currentDir = null;
        locToExplore = null;
        targetLocked = false;
    }
}