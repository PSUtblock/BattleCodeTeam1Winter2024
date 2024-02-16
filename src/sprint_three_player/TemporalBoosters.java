package sprint_three_player;
import battlecode.common.*;
import sprint_three_player.Communication;
import sprint_three_player.Movement;

public class TemporalBoosters {

    // Map locations to store headquarters, closest well, and island positions.
    private static MapLocation hqLocation;
    private static MapLocation wellLocation;
    private static MapLocation islandLocation;
    private static MapLocation myLocation;

    /**
     * Run a single turn for a Carrier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runTemporalBoosters(RobotController rc) throws GameActionException {
        myLocation = rc.getLocation();       // Get robot's current location.
    }
    }
