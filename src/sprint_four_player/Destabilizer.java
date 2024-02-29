package sprint_four_player;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Destabilizer {
    public static void runDestabilizer(RobotController rc) throws GameActionException {

        Communication.writeWells(rc);

        Communication.writeIslands(rc);

        Movement.explore(rc);
    }
}
