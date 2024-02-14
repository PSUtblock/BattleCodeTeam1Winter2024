package sprint_three_player;

import battlecode.common.*;
import sprint_three_player.Communication;
import sprint_three_player.Movement;

import java.util.HashSet;
import java.util.Set;

import static sprint_three_player.RobotPlayer.directions;
import static sprint_three_player.RobotPlayer.rng;

public class Launcher {

    /**
     * Run a single turn for a Launcher.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static int wellGuardTurns = 0;
    static boolean isChasing = false; // New variable to track if we are chasing an enemy
    static MapLocation wellLocation = null; // New variable to remember the well location
    public static void runLauncher(RobotController rc) throws GameActionException {
        int behaviorMode = (rc.getRoundNum() / 10) % 2;

        if (behaviorMode == 0) {
            // Follow ally carrier
            followAllyCarrier(rc);
        } else {
            // Attack enemy robots
            attackEnemies(rc);
        }
        if (wellGuardTurns > 0 || isChasing) {
            wellGuardTurns = isChasing ? wellGuardTurns : wellGuardTurns - 1;
            System.out.println("Guarding well at " + wellLocation + ", turns left: " + wellGuardTurns);
            // Sense for enemies while guarding the well
            RobotInfo[] enemiesTeam = rc.senseNearbyRobots(rc.getType().actionRadiusSquared, rc.getTeam().opponent());
            if (enemiesTeam.length > 0) {
                MapLocation enemyLocation = enemiesTeam[0].location;
                isChasing = true; // Start chasing
                //System.out.println("Chasing enemy at " + enemyLocation);
                Movement.moveToLocation(rc, rc.getLocation().directionTo(enemyLocation));
                if (rc.getLocation().isAdjacentTo(enemyLocation) && rc.canAttack(enemyLocation)) {
                    rc.attack(enemyLocation);
                }
                return; // Skip rest of the turn if chasing or guarding
            } else if (isChasing) {
                // Return to well and reset the guard timer
                if (rc.getLocation().equals(wellLocation)) {
                    wellGuardTurns = 5;
                    isChasing = false;
                } else {
                    Movement.moveToLocation(rc, rc.getLocation().directionTo(wellLocation));
                }
                return; // Skip rest of the turn if still guarding
            }
        }
        attackWithPriority(rc);

        // Attack logic
        attackEnemies(rc);

        followAndGuardAlly(rc);

        // Also try to move randomly.
        Direction dir = directions[rng.nextInt(directions.length)];
        Movement.moveToLocation(rc, dir);
        updateWellLocation(rc);

    }
    /**
     * Tracks and moves towards the nearest ally Carrier.
     */
    private static void followAllyCarrier(RobotController rc) throws GameActionException {
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots(); // Gets all robots within vision radius
        MapLocation closestCarrierLocation = null;

        if (nearbyRobots.length > 0) {
            Set<MapLocation> alliedCarriers = new HashSet<>();
            for (RobotInfo robot : nearbyRobots) {
                if (robot.getType() == RobotType.CARRIER && robot.getTeam() == rc.getTeam()) {
                    alliedCarriers.add(robot.getLocation());
                }
            }
            closestCarrierLocation = Movement.getClosestLocation(rc, alliedCarriers);
        }

        // If a Carrier is found, move towards it
        if (closestCarrierLocation != null && rc.isMovementReady()) {
            Direction directionToMove = rc.getLocation().directionTo(closestCarrierLocation);
            Movement.moveToLocation(rc, directionToMove);
        }
    }
    private static void attackEnemies(RobotController rc) throws GameActionException {
        RobotInfo[] enemies = rc.senseNearbyRobots(rc.getType().actionRadiusSquared, rc.getTeam().opponent());
        if (enemies.length > 0) {
            MapLocation toAttack = enemies[0].location;
            if (rc.canAttack(toAttack)) {
                rc.setIndicatorString("Attacking");
                rc.attack(toAttack);
            }
        }
    }

    private static void updateWellLocation(RobotController rc) throws GameActionException {
        // Refresh well locations and guard if on a well
        sprint_two_player.Communication.writeWells(rc);
        wellLocation = Communication.readWell(rc);
        if (rc.getLocation().equals(wellLocation)) {
            wellGuardTurns = 5; // Start guarding for 5 turns
        }
    }

    private static void attackWithPriority(RobotController rc) throws GameActionException {
        RobotInfo[] enemies = rc.senseNearbyRobots(rc.getType().actionRadiusSquared, rc.getTeam().opponent());
        // Group enemies by type with predefined priorities
        RobotInfo targetCarrier = null;
        RobotInfo targetLauncher = null;
        RobotInfo targetAmplifier = null;

        for (RobotInfo enemy : enemies) {
            switch (enemy.getType()) {
                case CARRIER:
                    if (targetCarrier == null)
                        targetCarrier = enemy; // Target the first Carrier seen
                    break;
                case LAUNCHER:
                    if (targetLauncher == null)
                        targetLauncher = enemy; // Target the first Launcher seen
                    break;
                case AMPLIFIER:
                    if (targetAmplifier == null)
                        targetAmplifier = enemy; // Target the first Amplifier seen
                    break;
            }
        }

        // Attack based on priority: Carriers -> Launchers -> Amplifiers
        if (targetCarrier != null && rc.canAttack(targetCarrier.location)) {
            rc.attack(targetCarrier.location);
        } else if (targetLauncher != null && rc.canAttack(targetLauncher.location)) {
            rc.attack(targetLauncher.location);
        } else if (targetAmplifier != null && rc.canAttack(targetAmplifier.location)) {
            rc.attack(targetAmplifier.location);
        }else {
            attackEnemies(rc);
        }
    }

    private static MapLocation lastAmplifierLocation = null;
    private static boolean isAssignedToAmplifier = false;

    private static void followAndGuardAlly(RobotController rc) throws GameActionException {
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots();

        // Find the closest Amplifier if not already assigned
        if (!isAssignedToAmplifier) {
            RobotInfo closestAmplifier = null;
            for (RobotInfo robot : nearbyRobots) {
                if (robot.getType() == RobotType.AMPLIFIER && robot.getTeam() == rc.getTeam()) {
                    if (closestAmplifier == null || rc.getLocation().distanceSquaredTo(robot.getLocation()) < rc.getLocation().distanceSquaredTo(closestAmplifier.getLocation())) {
                        closestAmplifier = robot;
                        isAssignedToAmplifier = true;
                    }
                }
            }

            if (closestAmplifier != null) {
                lastAmplifierLocation = closestAmplifier.getLocation();
            }
        }

        // If assigned to an Amplifier, check if it has moved
        if (isAssignedToAmplifier && lastAmplifierLocation != null) {
            RobotInfo currentAmplifier = null;
            for (RobotInfo robot : nearbyRobots) {
                if (robot.getType() == RobotType.AMPLIFIER && robot.getLocation().equals(lastAmplifierLocation)) {
                    currentAmplifier = robot;
                    break;
                }
            }

            // If the Amplifier is no longer at the last known location, it has moved
            if (currentAmplifier == null) {
                // Attempt to find and follow the Amplifier again
                isAssignedToAmplifier = false;
                lastAmplifierLocation = null;
                followAndGuardAlly(rc); // Retry finding an Amplifier
            } else {
                // Stay and guard the Amplifier
                attackWithPriority(rc); // Attack nearby enemies
            }
        } else {
            // No Amplifier to follow; engage in other behaviors
            attackWithPriority(rc);
        }
    }

}