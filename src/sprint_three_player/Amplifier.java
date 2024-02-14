package sprint_three_player;

import battlecode.common.*;
import sprint_two_player.Communication;
import sprint_two_player.Movement;

import java.util.HashSet;
import java.util.Set;

public class Amplifier {
    private static final int UNIT_SPACING = RobotType.AMPLIFIER.actionRadiusSquared; // Buffer spacing between amplifiers.

    // Map locations to store headquarters, closest well, and island positions.
    private static MapLocation hqLocation;
    private static MapLocation wellLocation;
    private static MapLocation islandLocation;

    public static void runAmplifier(RobotController rc) throws GameActionException {
        MapLocation myLocation = rc.getLocation();

        // If the headquarters have not already been found, locate the closest one.
        if (hqLocation == null) {
            hqLocation = Communication.readHQ(rc);
        }

        // If the closest well has not been found, locate it.
        if (wellLocation == null) {
            wellLocation = Communication.readWell(rc);
        }

        // If the closest island has not been found, locate it.
        if (islandLocation == null) {
            islandLocation = Communication.readIsland(rc);
        }

        RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
        Set<MapLocation> allyRobots = new HashSet<>();
        MapLocation closestAlly;
        for (RobotInfo robot : nearbyRobots) {
            if (robot.getTeam().isPlayer()
                    && (robot.getType() == RobotType.AMPLIFIER
                    || robot.getType() == RobotType.CARRIER
                    || robot.getType() == RobotType.LAUNCHER)) {
                allyRobots.add(robot.getLocation());
            }
        }
        closestAlly = Movement.getClosestLocation(rc, allyRobots);

        if (closestAlly != null) {
            if (myLocation.distanceSquaredTo(closestAlly) > UNIT_SPACING) {
                Movement.moveToLocation(rc, closestAlly);
            }
            else {
                int oppositeX = myLocation.directionTo(closestAlly).opposite().getDeltaX();
                int oppositeY = myLocation.directionTo(closestAlly).opposite().getDeltaY();
                MapLocation oppositeLoc = new MapLocation(myLocation.x + oppositeX, myLocation.y + oppositeY);
                Movement.moveToLocation(rc, oppositeLoc);
            }
        }
    }
}