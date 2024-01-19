package sprint_one_player;

import battlecode.common.*;

import java.util.Random;

public class Launcher {

    /** Stored values sent from RobotPlayer. Add or remove as needed **/
    private static Random rng;
    private static Direction[] directions;

    /** Constructor for Launchers. This establishes values passed from Robot Player That are used
     * at Headquarters. Add or remove parameters as needed **/
    public Launcher(Random rng, Direction[] directions)
    {
        this.rng = rng;
        this.directions = directions;
    }

    /**
     * Run a single turn for a Launcher.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runLauncher(RobotController rc) throws GameActionException {
        // Try to attack someone
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        if (enemies.length >= 0) {
            // MapLocation toAttack = enemies[0].location;
            MapLocation toAttack = rc.getLocation().add(Direction.EAST);

            if (rc.canAttack(toAttack)) {
                rc.setIndicatorString("Attacking");
                rc.attack(toAttack);
            }
        }

        // Also try to move randomly.
        Direction dir = directions[rng.nextInt(directions.length)];
        if (rc.canMove(dir)) {
            rc.move(dir);
        }
    }
}
