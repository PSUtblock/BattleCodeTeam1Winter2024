package sprint_two_player;

import battlecode.common.*;

import java.util.ArrayList;

/**
 * Currently, the communication array is set up to store x-y coordinate locations in x-y coordinate index pairings. For
 * well locations, a third index grouped with its location indices is utilized to represent a resource type, where 1
 * represents adamantium and 2 represents mana. Additionally, a single index is used to represent resources that
 * should be prioritized during a given time, where 1 prioritizes adamantium and 2 prioritizes mana.
 * The communication array is set up accordingly:
 * - Up to 4 headquarters stored from indices 0 to 7
 *      (i.e., index 0 and 1 represent x and y coordinate of a headquarter, respectively)
 * - Up to 4 wells and their resource type stored from indices 8 to 19
 *      (i.e., index 8, 9, and 10 represent x and y coordinate and resource type of a well, respectively)
 * - Up to 20 islands stored from indices 20 to 59
 *      (uses same x-y pairing method as with headquarters)
 * - Index 60 is reserved for which resource to prioritize
 * - Index 61 is reserved for behavioral state
 */
public class Communication {
    // Number of headquarters, wells, and islands allowed in communication array.
    private static final int NUM_HQ = GameConstants.MAX_STARTING_HEADQUARTERS;
    private static final int NUM_WELLS = 4;
    private static final int NUM_ISLANDS = 20;

    // Array index increment amounts.
    private static final int XY_IDX_INCREMENT = 2;
    private static final int WELL_IDX_INCREMENT = 3;

    // Starting index and index increment constants for reading and writing from specified location types.
    private static final int START_HQ_IDX = 0;
    private static final int START_WELL_IDX = NUM_HQ * XY_IDX_INCREMENT;
    private static final int START_ISLAND_IDX = START_WELL_IDX + (NUM_WELLS * WELL_IDX_INCREMENT);
    private static final int PRIORITY_IDX = START_ISLAND_IDX + (NUM_ISLANDS * XY_IDX_INCREMENT);
    private static final int STATE_IDX = PRIORITY_IDX + 1;

    // Read headquarter location closest to robot.
    public static MapLocation readHQ(RobotController rc) throws GameActionException {
        ArrayList<MapLocation> hqLocations = new ArrayList<>();
        MapLocation me = rc.getLocation();
        // Read all headquarters.
        for (int i = START_HQ_IDX; i < START_WELL_IDX; i += XY_IDX_INCREMENT) {
            if (rc.readSharedArray(i) != 0) {
                hqLocations.add(new MapLocation(rc.readSharedArray(i), rc.readSharedArray(i + 1)));
            }
        }
        // Return the closest headquarter or return null.
        MapLocation closestHQ = null;
        if (!hqLocations.isEmpty()) {
            closestHQ = hqLocations.get(0);
            int minDistance = me.distanceSquaredTo(closestHQ);
            for (int i = 1; i < hqLocations.size(); ++i) {
                int currDistance = me.distanceSquaredTo(hqLocations.get(i));
                if (minDistance > currDistance) {
                    minDistance = currDistance;
                    closestHQ = hqLocations.get(i);
                }
            }
        }
        return closestHQ;
    }

    // Write headquarter location to array.
    public static void writeHQ(RobotController rc) throws GameActionException {
        MapLocation hqLoc = null;
        RobotInfo[] robots = rc.senseNearbyRobots();

        // Check for this teams Headquarters and record its location.
        for (RobotInfo robot : robots) {
            if ((robot.getTeam() == rc.getTeam()) && (robot.getType() == RobotType.HEADQUARTERS)) {
                hqLoc = robot.getLocation();
                break;
            }
        }
        for (int i = START_HQ_IDX; i < START_WELL_IDX; i += XY_IDX_INCREMENT) {
            if (rc.readSharedArray(i) == 0 && hqLoc != null && rc.canWriteSharedArray(i, hqLoc.x)) {
                rc.writeSharedArray(i, hqLoc.x);
                rc.writeSharedArray(i + 1, hqLoc.y);
                break;
            }
        }
    }
}
