package terrabot.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import terrabot.TerraBot;
import terrabot.entities.Position;
import terrabot.map.Cell;
import terrabot.map.SimMap;

import java.util.function.Function;

public final class MoveRobot {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private MoveRobot() { }

    /**
     * Moves the robot to the nearby cell with the lowest risk score,
     * and returns a JSON node containing the move message.
     *
     * @param robot the TerraBot instance
     * @param simMap   the simulation map
     * @return ObjectNode with "message" field
     */
    public static ObjectNode move(final TerraBot robot, final SimMap simMap) {

        final Position currPos = robot.getPosition();
        final int x = currPos.getX();
        final int y = currPos.getY();

        final int width = simMap.getColumns();
        final int height = simMap.getRows();

        int bestScore = Integer.MAX_VALUE;
        int bestX = x;
        int bestY = y;
        boolean wasPossibleToMove = false;

        // lambda to compute the cost of moving to a specific cell
        Function<Cell, Integer> score = cell -> {
            double tempScore = 0.0;
            int count = 0;
            if (cell.getSoil() != null) {
                tempScore += cell.getSoil().computeStuckChance();
                ++count;
            }
            if (cell.getAir() != null) {
                tempScore += cell.getAir().computeToxicity();
                ++count;
            }
            if (cell.getAnimal() != null) {
                tempScore += cell.getAnimal().calculateAttackChance();
                ++count;
            }
            if (cell.getPlant() != null) {
                tempScore += cell.getPlant().getStuckChance();
                ++count;
            }

            if (count == 0) {
                return 0;
            }
            double mean =  Math.abs(tempScore / count);
            return (int) Math.round(mean);
        };

        if (y + 1 < height) {
            Cell up = simMap.getCell(x, y + 1);
            int s = score.apply(up);
            if (s < bestScore && s <= robot.getEnergyStatus()) {
                bestScore = s;
                bestX = x;
                bestY = y + 1;
                wasPossibleToMove = true;
            }
        }

        if (x + 1 < width) {
            Cell right = simMap.getCell(x + 1, y);
            int s = score.apply(right);
            if (s < bestScore && s <= robot.getEnergyStatus()) {
                bestScore = s;
                bestX = x + 1;
                bestY = y;
                wasPossibleToMove = true;
            }
        }

        if (y - 1 >= 0) {
            Cell down = simMap.getCell(x, y - 1);
            int s = score.apply(down);
            if (s < bestScore && s <= robot.getEnergyStatus()) {
                bestScore = s;
                bestX = x;
                bestY = y - 1;
                wasPossibleToMove = true;
            }
        }

        if (x - 1 >= 0) {
            Cell left = simMap.getCell(x - 1, y);
            int s = score.apply(left);
            if (s < bestScore && s <= robot.getEnergyStatus()) {
                bestScore = s;
                bestX = x - 1;
                bestY = y;
                wasPossibleToMove = true;
            }
        }

        if (wasPossibleToMove) {
            robot.setPosition(new Position(bestX, bestY));
            robot.setEnergyStatus(robot.getEnergyStatus() - bestScore);
            ObjectNode res = MAPPER.createObjectNode();
            res.put("message",
                    "The robot has successfully moved to position ("
                            + bestX + ", " + bestY + ").");

            return res;
        } else {
            ObjectNode res = MAPPER.createObjectNode();
            res.put("message",
                    "ERROR: Not enough battery left. Cannot perform action");
            return res;
        }
    }
}
