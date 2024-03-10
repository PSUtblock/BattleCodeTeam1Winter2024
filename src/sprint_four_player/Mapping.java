package sprint_four_player;

import battlecode.common.*;
import java.util.*;

public class Mapping {
    /**
     * Return the closest location with respect to robot's current location. Uses an array of MapLocations as a parameter.
     **/
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

    /**
     * Return the closest location with respect to robot's current location. Uses set of MapLocations as a parameter.
     **/
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

    /**
     * Return the closest location with respect to robot's current location. Uses list of MapLocations as a parameter.
     **/
    public static MapLocation getClosestLocation(RobotController rc, List<MapLocation> locations) {
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
    /**
     * Fills a list with possible landmarks spaced out by 100 units
     **/
    public static List<MapLocation> getPossibleLandmarks(RobotController rc, int mapWidth, int mapHeight, int radius) throws GameActionException {
        List<MapLocation> potentialLandmarks = new ArrayList<>();
        for (int x = 0; x < mapWidth; x += radius - 1) {
            for (int y = 0; y < mapHeight; y += radius - 1) {
                MapLocation potentialLocation = new MapLocation(x, y);
                potentialLandmarks.add(potentialLocation);
            }
        }
        return sortLocations(rc, potentialLandmarks);
    }

    /**
     * Sorts locations with respect to distance from a headquarters or from self if necessary
     **/
    private static List<MapLocation> sortLocations(RobotController rc, List<MapLocation> locations) throws GameActionException {
        MapLocation centerLoc = findCentralLocation(rc);
        locations.sort(new DistanceComparator(centerLoc));
        return locations;
    }

    /** Returns headquarters or robots location to be used as a central location **/
    private static MapLocation findCentralLocation(RobotController rc) throws GameActionException {
        MapLocation centerLoc = Communication.readHQ(rc);
        return (centerLoc == null) ? rc.getLocation() : centerLoc;
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