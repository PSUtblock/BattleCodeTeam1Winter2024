package sprint_three_player;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.Set;

/**
 * Unify movement capabilities into one class.
 */
public class Movement {
    private static final int NUM_DIRS = 8; // Total number of directions not including CENTER.
    private static Direction currentDir = null;

    /** Move the robot in a given direction. **/
    public static void moveToLocation(RobotController rc, Direction dir) throws GameActionException {
        // If the robot can move, move in that direction. Otherwise, try to move randomly.
        if (rc.canMove(dir)) {
            rc.move(dir);
        }
    }

    /** Move the robot to a given location. **/
    public static void moveToLocation(RobotController rc, MapLocation location) throws GameActionException {
        // Check that robot is not already at the location and that movement is ready.
        if (!rc.getLocation().equals(location) && rc.isMovementReady()) {
            Direction dir = rc.getLocation().directionTo(location);
            // If robot can move, then there are no obstacles in this direction.
            if (rc.canMove(dir)) {
                rc.move(dir);
                currentDir = null;
            }
            else {
                // Otherwise, there is an obstacle we must go around (clockwise).
                if (currentDir == null) {
                    currentDir = dir;
                }
                // For all possible directions, try to move in a clockwise motion around the obstacle.
                currentDir = moveClockwise(rc, currentDir);
            }
        }
    }

    /** Turn clockwise around an obstacle **/
    public static Direction moveClockwise(RobotController rc, Direction currentDir) throws GameActionException{
        for (int i = 0; i < NUM_DIRS; ++i) {
            // If no obstacle, turn right to face toward target location again.
            if (rc.canMove(currentDir)) {
                rc.move(currentDir);
                return currentDir.rotateRight();
            }
            // Otherwise, turn left to go around the obstacle clockwise.
            currentDir = currentDir.rotateLeft();
        }
        return currentDir;
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
}