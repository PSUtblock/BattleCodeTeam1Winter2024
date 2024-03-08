package sprint_four_player;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

import java.util.Objects;
import java.util.Random;

/**
 * Temporal Booster Robot
 * The Booster Robot will boost the abilities of its teammates within its radius of influence.
 * When spawned, the booster will be given a random behavior state and will perform the specific
 * functions within that state.
 *
 * State 1: Booster will only befriend a Carrier and boost its area.
 * State 2: Booster will only befriend a Launcher and boost its area.
 * State 3: Booster will not make a friend, and will move randomly, boosting its area.
 */

public class TemporalBoosters {

    // ID of the Boosters Best Friend
    private static int roboBuddy = -1;
    /**
     * Random number to define Booster Behavior
     */
    private static int randomBehavior = new Random().nextInt(3) + 1;


    /**
     * Run a single turn for a Booster
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runTemporalBoosters(RobotController rc) throws GameActionException {

        findFriend(rc); // finds the ID of a friendly robot, Carrier or Launcher determined by spawned behavior state.
        boostFriendArea(rc); // Looks for the robot friend, follows them, and boosts them. If no friend or doesn't want a friend randomly move and boost.
    }

    /**
     * Boost the area, if it is possible to boost.
     */
    public static void boostArea(RobotController rc) throws GameActionException {
        if(rc.canBoost()) {
            rc.boost();
        }
    }

    /**
     * Find a Robot Friend on your team. Uses the Behavior state to determine what type of friend the
     * Booster will make.
     */
    public static void findFriend(RobotController rc) {
        RobotInfo[] nearbyRobots;

        // check if we need a friend. If -1 we have no friend.
        if(randomBehavior != 3 && roboBuddy == -1) {
            nearbyRobots = rc.senseNearbyRobots();

            for (RobotInfo nearbyRobot : nearbyRobots) {

                switch (randomBehavior) {
                    case 1:
                        if (nearbyRobot.team == rc.getTeam() && Objects.equals(nearbyRobot.getType().toString(), "CARRIER")) {
                            roboBuddy = nearbyRobot.ID;
                            rc.setIndicatorString("My Carrier Friend Is: " + roboBuddy);
                            break;
                        }
                        else {
                            rc.setIndicatorString("Couldn't find a friendly Carrier");
                        }
                        break;
                    case 2:
                        if (nearbyRobot.team == rc.getTeam() && Objects.equals(nearbyRobot.getType().toString(), "LAUNCHER")) {
                            roboBuddy = nearbyRobot.ID;
                            rc.setIndicatorString("My Launcher Friend Is: " + roboBuddy);
                            break;
                        }
                        else {
                            rc.setIndicatorString("Couldn't find a friendly Launcher");
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Robot looks for his friend and follows them around boosting their abilities.
     * The Booster will also stay at a distance from their friends location to stay out of the way.
     * If no robot friends are found or the friend that it was following, the Booster will
     * wander around until it finds another friend.
     */
    public static void boostFriendArea(RobotController rc) throws GameActionException {

        if( roboBuddy != -1) {
            if(rc.canSenseRobot(roboBuddy))
            {
                Movement.moveToLocation(rc, rc.senseRobot(roboBuddy).getLocation().translate(4,1));
            }
            else {
                rc.setIndicatorString("I lost my friend :(");
                roboBuddy = -1;
                Movement.moveRandomly(rc);
            }

        }

        if( randomBehavior == 3)
        {
            rc.setIndicatorString("I don't need a friend");
            Movement.moveRandomly(rc);
        }

        boostArea(rc);

    }
}



