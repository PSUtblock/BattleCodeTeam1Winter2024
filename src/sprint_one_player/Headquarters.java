package sprint_one_player;

import battlecode.common.*;

//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Random;

import static sprint_one_player.RobotPlayer.directions;
import static sprint_one_player.RobotPlayer.rng;
//import java.util.Set;

public class Headquarters {


    /**
     * Run a single turn for a Headquarters.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runHeadquarters(RobotController rc) throws GameActionException {
        // Pick a direction to build in.
        Direction dir = directions[rng.nextInt(directions.length)];
        MapLocation newLoc = rc.getLocation().add(dir);
        if (rc.canBuildAnchor(Anchor.STANDARD)) {
            // If we can build an anchor do it!
            rc.buildAnchor(Anchor.STANDARD);
            rc.setIndicatorString("Building anchor! " + rc.getNumAnchors(null));
        }
        if (rng.nextBoolean()) {
            // Let's try to build a carrier.
            rc.setIndicatorString("Trying to build a carrier");
            if (rc.canBuildRobot(RobotType.CARRIER, newLoc)) {
                rc.buildRobot(RobotType.CARRIER, newLoc);
            }
        } else {
            // Let's try to build a launcher.
            rc.setIndicatorString("Trying to build a launcher");
            if (rc.canBuildRobot(RobotType.LAUNCHER, newLoc)) {
                rc.buildRobot(RobotType.LAUNCHER, newLoc);
            }
        }
    }
}
