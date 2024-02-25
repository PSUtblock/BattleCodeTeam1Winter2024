package sprint_three_player;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

import java.util.*;

import static sprint_three_player.RobotPlayer.directions;
import static sprint_three_player.RobotPlayer.rng;

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
                for (int i = 0; i < NUM_DIRS; ++i) {
                    // If no obstacle, turn right to face toward target location again.
                    if (rc.canMove(currentDir)) {
                        rc.move(currentDir);
                        currentDir = currentDir.rotateRight();
                        break;
                    }
                    // Otherwise, turn left to go around the obstacle clockwise.
                    currentDir = currentDir.rotateLeft();
                }
            }
        }
    }

    /** Turn clockwise around an obstacle **/
    public static Direction moveClockwise(RobotController rc) throws GameActionException{
        currentDir = rc.getLocation().directionTo(new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2));
        for (int i = 0; i < NUM_DIRS; ++i) {
            // If no obstacle, turn right to face toward target location again.
            if (rc.canMove(currentDir)) {
                return currentDir;
            }
            // Otherwise, turn left to go around the obstacle clockwise.
            currentDir = currentDir.rotateRight();
        }
        return currentDir;
    }

    /** Turn counterclockwise around an obstacle **/
    public static Direction moveCounterClockwise(RobotController rc) throws GameActionException{
        currentDir = rc.getLocation().directionTo(new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2));
        for (int i = 0; i < NUM_DIRS; ++i) {
            // If no obstacle, turn right to face toward target location again.
            if (rc.canMove(currentDir)) {
                return currentDir;
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

    /** Move using A* search **/
    public static void aStarMove(RobotController rc, MapLocation target) throws GameActionException {
        MapLocation currLocation = rc.getLocation();

        if (currLocation.equals(target) || !rc.isMovementReady()) {
            return;
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<MapLocation> closedSet = new HashSet<>();

        Node start = new Node(currLocation, 0, currLocation.distanceSquaredTo(target), null);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.getLocation().equals(target)) {
                Node next = current;
                while (next != null && next.getParent() != null) {
                    Direction dir = currLocation.directionTo(next.getLocation());
                    if (rc.canMove(dir) && rc.isMovementReady()) {
                        rc.move(dir);
                        currLocation = rc.getLocation();
                    }
                    else {
                        break;
                    }
                    next = next.getParent();
                }
                return;
            }

            closedSet.add(current.getLocation());

            for (Direction dir : Direction.values()) {
                MapLocation neighborLoc = current.getLocation().add(dir);
                if (!rc.canMove(dir) || closedSet.contains(neighborLoc)) {
                    continue;
                }

                int newCost = current.getCost() + 1;
                int heuristic = neighborLoc.distanceSquaredTo(target);
                Node neighbor = new Node(neighborLoc, newCost, heuristic, current);

                if (!openSet.contains(neighbor) || newCost < neighbor.getCost()) {
                    openSet.add(neighbor);
                }
            }

            // If cannot move, try primitive moves.
//            Direction randDir = directions[rng.nextInt(directions.length)];
//            if (rc.canMove(randDir)) {
//                moveToLocation(rc, randDir);
//            }
//            else {
                moveToLocation(rc, target);
//            }
        }
    }

    /** Class to contain a node **/
    private static class Node implements Comparable<Node> {
        private final MapLocation location;
        private final int cost;
        private final int heuristic;
        private final Node parent;

        public Node(MapLocation location, int cost, int heuristic, Node parent) {
            this.location = location;
            this.cost = cost;
            this.heuristic = heuristic; //Euclidean distance
            this.parent = parent;
        }

        public MapLocation getLocation() {
            return location;
        }

        public int getCost() {
            return cost;
        }

        public int getHeuristic() {
            return heuristic;
        }

        public Node getParent() {
            return parent;
        }

        @Override
        public int compareTo(Node other) {
            int totalCost = cost + heuristic;
            int otherTotalCost = other.cost + other.heuristic;
            return Integer.compare(totalCost, otherTotalCost);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Node node = (Node) obj;
            return location.equals(node.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location);
        }
    }

    public static void moveTowards(RobotController rc, MapLocation target) throws GameActionException {
        MapLocation currentLoc = rc.getLocation();
        if (currentLoc.equals(target)) {
            return;
        }

        Direction dirToTarget = currentLoc.directionTo(target);

        if (rc.canMove(dirToTarget)) {
            rc.move(dirToTarget);
        }
        else {
            bugTwoNav(rc, target);
        }
    }

    private static void bugTwoNav(RobotController rc, MapLocation target) throws GameActionException {
        MapLocation currentLoc = rc.getLocation();
        Direction dirToTarget = currentLoc.directionTo(target);

        int distToTarget = currentLoc.distanceSquaredTo(target);
        int initialDist = distToTarget;

        Direction bestDir = null;
        int bestDist = (60 * 60) + 1; // Nothing greater than max map size plus 1.

        for (Direction dir : Direction.values()) {
            if (rc.canMove(dir)) {
                MapLocation newLocation = currentLoc.add(dir);
                int newDist = newLocation.distanceSquaredTo(target);

                if (newDist < bestDist) {
                    bestDir = dir;
                    bestDist = newDist;
                }
            }
        }

        if (bestDist < initialDist && rc.canMove(bestDir)) {
            rc.move(bestDir);
        }
        else {
            Direction nextDir = dirToTarget.rotateRight().rotateRight();
            if (rc.canMove((nextDir))) {
                rc.move(nextDir);
            }
        }
    }

    public static void explore(RobotController rc) throws GameActionException {
        Direction exploreDir = directions[rng.nextInt(directions.length)];

        if (rc.canMove(exploreDir)) {
            rc.move(exploreDir);
        }
        else {
            exploreDir = directions[rng.nextInt(directions.length)];
            if (rc.canMove(exploreDir)) {
                rc.move(exploreDir);
            }
            else {
                Direction clockwiseDir = moveClockwise(rc);
                if (rc.canMove(clockwiseDir)) {
                    rc.move(clockwiseDir);
                }
                else {
                    Direction counterClockwiseDir = moveCounterClockwise(rc);
                    if (rc.canMove(counterClockwiseDir)) {
                        rc.move(counterClockwiseDir);
                    }
                }
            }
        }
    }
}
