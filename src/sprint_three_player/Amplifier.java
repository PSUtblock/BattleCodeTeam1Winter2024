package sprint_three_player;

import battlecode.common.*;

import java.util.HashSet;
import java.util.Set;

public class Amplifier {
    private static final int UNIT_SPACING = RobotType.AMPLIFIER.actionRadiusSquared; // Buffer spacing between amplifiers.

    public static void runAmplifier(RobotController rc) throws GameActionException {
        MapLocation myLocation = rc.getLocation();
        boolean isAmplified = false;

        Communication.writeWells(rc);
        // Locate closest well.
        // Map locations to store headquarters, closest well, and island positions.
        MapLocation wellLocation = Communication.readWell(rc, 0);

        Communication.writeIslands(rc);
        MapLocation islandLocation = Communication.readIsland(rc, 0);

        RobotInfo[] robots = rc.senseNearbyRobots();
        Set<MapLocation> allyAmplifiers = new HashSet<>();
        for (RobotInfo robot : robots) {
            if (robot.getType() == RobotType.AMPLIFIER && robot.getTeam() == rc.getTeam()) {
                allyAmplifiers.add(robot.getLocation());
            }
        }

        if (wellLocation != null) {
            if (myLocation.distanceSquaredTo(wellLocation) > UNIT_SPACING){
                for (MapLocation robot : allyAmplifiers) {
                    if (robot.distanceSquaredTo(wellLocation) < myLocation.distanceSquaredTo(wellLocation)) {
                        isAmplified = true;
                        break;
                    }
                }
                if (isAmplified) {
                    Movement.explore(rc);
                }
                else {
                    Movement.moveToLocation(rc, wellLocation);
                }
            }
            else {
                Movement.explore(rc);
            }
        }
        else if (islandLocation != null && myLocation.distanceSquaredTo(islandLocation) > UNIT_SPACING) {
            for (MapLocation robot : allyAmplifiers) {
                if (robot.distanceSquaredTo(islandLocation) < myLocation.distanceSquaredTo(islandLocation)) {
                    isAmplified = true;
                    break;
                }
            }
            if (isAmplified) {
                Movement.explore(rc);
            }
            else {
                Movement.moveToLocation(rc, islandLocation);
            }
        }
        else {
            Movement.explore(rc);
        }
    }
}