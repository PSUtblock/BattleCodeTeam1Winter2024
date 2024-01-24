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

        // attempting to move to the defined space 'testWell' position (9,14) that is defined in the RobotPlayer.java class
        /* The launcher will move to the testWell location and once the location is within the actionable radius, it will start to move randomly
        until the testWell is no longer within its actionable radius
        the path movement is simplified.
         */
        while(rc.isMovementReady())
        {
            MapLocation currentLocation;

            while(!rc.canActLocation(testWell))
            {
                currentLocation = rc.getLocation();
                rc.setIndicatorString("Moving to test location");

                if(currentLocation.x < testWell.x)
                {
                    if (rc.canMove(Direction.EAST)) {
                        rc.move(Direction.EAST);
                    }
                }
                else if (currentLocation.x > testWell.x) {
                    if (rc.canMove(Direction.WEST)) {
                        rc.move(Direction.WEST);
                    }
                }

                if(currentLocation.y < testWell.y)
                {
                    if (rc.canMove(Direction.NORTH)) {
                        rc.move(Direction.NORTH);
                    }
                }
                else if (currentLocation.y > testWell.y) {
                    if (rc.canMove(Direction.SOUTH)) {
                        rc.move(Direction.SOUTH);
                    }
                }

            }

            // Also try to move randomly.
            Direction dir = directions[rng.nextInt(directions.length)];
            if (rc.canMove(dir)) {
                rc.move(dir);
            }
        }


    }
}
