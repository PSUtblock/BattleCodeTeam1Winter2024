package sprint_two_player;

import battlecode.common.*;

import static sprint_two_player.RobotPlayer.directions;
import static sprint_two_player.RobotPlayer.rng;
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
        int roundNum = rc.getRoundNum();
        int spawn_interval_anchor = 10;
        int spawn_interval_carrier = 4;
        int spawn_interval_launcher = 6;
        int spawn_anchor_fail = 0;
        int spawn_carrier_fail = 0;
        int spawn_launcher_fail = 0;

        // Write Headquarters location to shared array if first turn.
        if (roundNum == 1) {
            Communication.writeHQ(rc);
        }

        /**
         * For every 10 rounds, build an anchor
         */
        if(roundNum % spawn_interval_anchor == 0){
            if (rc.canBuildAnchor(Anchor.STANDARD)) {
                // If we can build an anchor do it!
                rc.buildAnchor(Anchor.STANDARD);
                rc.setIndicatorString("Building anchor! " + rc.getNumAnchors(null));
            }
            else{
                spawn_anchor_fail ++;
            }
        }
        if (rng.nextBoolean()) {
            /**
             * For every 4 rounds, create a carrier
             */
            if(roundNum % spawn_interval_carrier == 0 ){
                // Let's try to build a carrier.
                rc.setIndicatorString("Trying to build a carrier");
                if (rc.canBuildRobot(RobotType.CARRIER, newLoc)) {
                    rc.buildRobot(RobotType.CARRIER, newLoc);
                }
                else{
                    spawn_carrier_fail ++;
                }
            }
        } else {
            /**
             * For every 6 rounds, create a launcher
             */
            if(roundNum % spawn_interval_launcher == 0 ) {
                // Let's try to build a launcher.
                rc.setIndicatorString("Trying to build a launcher");
                if (rc.canBuildRobot(RobotType.LAUNCHER, newLoc)) {
                    rc.buildRobot(RobotType.LAUNCHER, newLoc);
                }
                else{
                    spawn_launcher_fail++;
                }
            }
        }
    }
}