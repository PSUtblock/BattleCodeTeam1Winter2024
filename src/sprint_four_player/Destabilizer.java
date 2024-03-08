package sprint_four_player;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class Destabilizer {
    public static void senseEnemyRobotsAndDestabilize(RobotController rc)throws GameActionException{
        // Sense enemy robots and destabilize them.
        RobotInfo[] enemies = rc.senseNearbyRobots(rc.getType().actionRadiusSquared,rc.getTeam().opponent());
        if (enemies.length > 0) {
            MapLocation enemyLoc = enemies[0].location;
            if (rc.canDestabilize(enemyLoc)) {
                rc.setIndicatorString("Destabilizing");
                rc.destabilize(enemyLoc);
            }
        }
    }
    public static void runDestabilizer(RobotController rc) throws GameActionException {

        Communication.writeWells(rc);

        Communication.writeIslands(rc);

        Movement.explore(rc);

        senseEnemyRobotsAndDestabilize(rc);
    }
}
