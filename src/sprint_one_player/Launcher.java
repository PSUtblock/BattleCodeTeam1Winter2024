package sprint_one_player;

import battlecode.common.*;

import static sprint_one_player.RobotPlayer.*;

public class Launcher {

    /**
     * Run a single turn for a Launcher.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runLauncher(RobotController rc) throws GameActionException {
        // Try to attack someone
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
        if (enemies.length > 0) {
            MapLocation toAttack = enemies[0].location;
            //MapLocation toAttack = rc.getLocation().add(Direction.EAST);

            if (rc.canAttack(toAttack)) {
                rc.setIndicatorString("Attacking");
                rc.attack(toAttack);
            }
        }

        // Additional functionality to track and follow an ally Carrier
        followAllyCarrier(rc);

        // Also try to move randomly.
        Direction dir = directions[rng.nextInt(directions.length)];
        if (rc.canMove(dir)) {
            rc.move(dir);
        }
    //}
    }
    /**
     * Tracks and moves towards the nearest ally Carrier.
     */
    private static void followAllyCarrier(RobotController rc) throws GameActionException {
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots(); // Gets all robots within vision radius
        MapLocation closestCarrierLocation = null;
        double minDistance = Double.MAX_VALUE;

        for (RobotInfo robot : nearbyRobots) {
            if (robot.getType() == RobotType.CARRIER && robot.getTeam() == rc.getTeam()) {
                double distance = rc.getLocation().distanceSquaredTo(robot.getLocation());
                if (distance < minDistance) {
                    closestCarrierLocation = robot.getLocation();
                    minDistance = distance;
                }
            }
        }

        // If a Carrier is found, move towards it
        if (closestCarrierLocation != null && rc.isMovementReady()) {
            Direction directionToMove = rc.getLocation().directionTo(closestCarrierLocation);
            if (rc.canMove(directionToMove)) {
                rc.move(directionToMove);
            }
        }
    }

}
