package sprint_two_player;

import battlecode.common.*;

import java.util.Set;

import static sprint_two_player.RobotPlayer.directions;
import static sprint_two_player.RobotPlayer.rng;

/**
 * Unify movement capabilities into one class.
 */
public class Movement {
    /** Move the robot in a given direction. **/
    public static void moveToLocation(RobotController rc, Direction dir) throws GameActionException {
        Direction randDir = directions[rng.nextInt(directions.length)];
        // If the robot can move, move in that direction. Otherwise, try to move randomly.
        if (rc.canMove(dir)) {
            rc.move(dir);
        }
        else if (rc.canMove(randDir)) {
            rc.move(randDir);
        }
    }

    /** Return the closest location with respect to robot's current location. Uses an array of MapLocations as a parameter. **/
    public static MapLocation getClosestLocation(RobotController rc, MapLocation[] locations) throws GameActionException{
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
    public static MapLocation getClosestLocation(RobotController rc, Set<MapLocation> locations) throws GameActionException {
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