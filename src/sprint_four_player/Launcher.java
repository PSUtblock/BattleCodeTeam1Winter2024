package sprint_four_player;

import battlecode.common.*;

import static sprint_four_player.RobotPlayer.directions;
import static sprint_four_player.RobotPlayer.rng;

public class Launcher {

    /**
     * Run a single turn for a Launcher.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runLauncher(RobotController rc) throws GameActionException {
        Communication.writeWells(rc);

        Communication.writeIslands(rc);

        followAndProtectCarrier(rc);

        moveTowardsOccupiedIslandsAndAttack(rc);

        attackWithPriority(rc);

        attackEnemies(rc);

        // Also try to move randomly.
        Direction dir = directions[rng.nextInt(directions.length)];
        Movement.moveToLocation(rc, dir);
        attackEnemies(rc);
    }

    private static void followAndProtectCarrier(RobotController rc) throws GameActionException {
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
        MapLocation closestCarrierLocation = null;
        boolean carrierHasAnchors = false;

        // First, find the closest carrier with anchors
        for (RobotInfo robot : nearbyRobots) {
            if (robot.getType() == RobotType.CARRIER && robot.getTeam() == rc.getTeam()) {
                // Assuming getAnchor() method exists and indicates whether the carrier has anchors
                int anchorCount = robot.getTotalAnchors();
                if (anchorCount > 0) {
//                    System.out.println("anchor count" +anchorCount);
                    closestCarrierLocation = robot.getLocation();
                    carrierHasAnchors = true;
                    break; // Found the carrier to follow
                }
            }
        }

        // If a carrier with anchors is found, attempt to move towards it or protect it
        if (carrierHasAnchors) {
            // Simultaneously check for and attack enemies if any are within action radius
            RobotInfo[] enemies = rc.senseNearbyRobots(rc.getType().actionRadiusSquared, rc.getTeam().opponent());
            if (enemies.length > 0) {
                // Prioritize attacking
                attackWithPriority(rc);
            } else {
                Movement.moveToLocation(rc, closestCarrierLocation);
                attackWithPriority(rc);
            }
        }
    }

    public static void attackEnemies(RobotController rc) throws GameActionException {
        RobotInfo[] enemies = rc.senseNearbyRobots(rc.getType().actionRadiusSquared, rc.getTeam().opponent());
        if (enemies.length > 0) {
            MapLocation toAttack = enemies[0].location;
            if (rc.canAttack(toAttack)) {
                rc.setIndicatorString("Attacking");
                rc.attack(toAttack);
            }
        }
    }

    public static void attackWithPriority(RobotController rc) throws GameActionException {
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
    static int allyLauncherCount = 0;
    public static void moveTowardsOccupiedIslandsAndAttack(RobotController rc) throws GameActionException {
        MapLocation closestOccupiedIsland = null;
        double closestDistance = Double.MAX_VALUE;

        for (int i = 16; i < 46; i++) {
            int data = rc.readSharedArray(i);
            if (data != 0) {
                int[] unpackedData = Packing.unpackObject(rc, data);
                if (unpackedData.length >= 3) {
                    int x = unpackedData[0];
                    int y = unpackedData[1];
                    int state = unpackedData[2];

                    if (state == 1) {
                        MapLocation islandLocation = new MapLocation(x, y);
                        double distance = rc.getLocation().distanceSquaredTo(islandLocation);
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestOccupiedIsland = islandLocation;
                        }
                    }
                }
            }
        }
        if (closestOccupiedIsland != null) {
            // Sense nearby allied launchers
            RobotInfo[] nearbyAllies = rc.senseNearbyRobots(rc.getType().actionRadiusSquared, rc.getTeam());

            for (RobotInfo ally : nearbyAllies) {
                if (ally.type == RobotType.LAUNCHER) {
                    allyLauncherCount++;
                }
            }

            if (allyLauncherCount <=5) {
                Movement.moveToLocation(rc, closestOccupiedIsland);
                attackEnemies(rc);
            }
            else{
                //attackEnemies(rc);
                if(rc.senseIsland(rc.getLocation())!=-1){
                    Movement.moveToLocation(rc,rc.getLocation());
                    attackEnemies(rc);
                }
                else {
                    Direction dir = directions[rng.nextInt(directions.length)];
                    Movement.moveToLocation(rc, dir);
                    attackEnemies(rc);
                }
            }
            attackWithPriority(rc);
        }
    }
}