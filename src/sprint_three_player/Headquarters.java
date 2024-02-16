package sprint_three_player;

import battlecode.common.*;
import sprint_three_player.Communication;

import static sprint_three_player.RobotPlayer.directions;
import static sprint_three_player.RobotPlayer.rng;
//import java.util.Set;

public class Headquarters {
    /**
     * Run a single turn for a Headquarters.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runHeadquarters(RobotController rc) throws GameActionException {
        // Pick a direction to build in.
        MapLocation hqLoc = rc.getLocation();
        Direction dir = directions[rng.nextInt(directions.length)];
        MapLocation newLoc = rc.getLocation().add(dir);
        int roundNum = rc.getRoundNum();
        int spawn_interval_anchor = 10;
        int spawn_interval_carrier = 10;
        int spawn_interval_launcher = 6;
        int spawn_interval_amplifier = 50;
        int spawn_interval_booster = 50;
        int spawn_anchor_fail = 0;
        int spawn_carrier_fail = 0;
        int spawn_launcher_fail = 0;
        int spawn_amplifier_fail = 0;
        int spawn_booster_fail = 0;


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
        //if (rng.nextBoolean()) {
        /**
         * For every 10 rounds, create a carrier
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

        /**
         * For every 6 rounds, create a launcher
         */
        if(roundNum % spawn_interval_launcher == 0 ) {
            // Let's try to build a launcher.
            rc.setIndicatorString("Trying to build a launcher");
            if (rc.canBuildRobot(RobotType.LAUNCHER, newLoc)) {
                rc.buildRobot(RobotType.LAUNCHER, newLoc);
            } else {
                spawn_launcher_fail++;
            }
        }
        /**
         * For every 50 rounds, create an amplifier
         */
        if(roundNum % spawn_interval_amplifier == 0 ){
            MapLocation amplifierLoc = new MapLocation(newLoc.x + 1, newLoc.y + 1);
            rc.setIndicatorString("Trying to build an amplifier");
            if (rc.canBuildRobot(RobotType.AMPLIFIER, amplifierLoc)) {
                rc.buildRobot(RobotType.AMPLIFIER, amplifierLoc);
            }
            else{
                spawn_amplifier_fail++;
            }
        }
        /**
         * For every 50 rounds, create a temporal booster
         */
        if(roundNum % spawn_interval_booster == 0 ){
            // Let's try to build a carrier.
            rc.setIndicatorString("Trying to build a temporal booster");
            if (rc.canBuildRobot(RobotType.BOOSTER, newLoc)) {
                rc.buildRobot(RobotType.BOOSTER, newLoc);
            }
            else{
                spawn_booster_fail ++;
            }
        }

    }
}