package terrabot.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import terrabot.TerraBot;
import terrabot.entities.Position;
import terrabot.map.Cell;
import terrabot.map.Map;

public final class ScanObject {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int ENERGY_REQUIRED = 7;

    private ScanObject() { }

    /**
     * Creates a JSON object describing the scanned object on the current cell
     * @param robot the TerraBot whose current position is inspected
     * @param map the map containing all territory cells
     * @param cmd the current command
     * @return an ObjectNode containing info about the scanned object
     */
    public static ObjectNode scan(final TerraBot robot, final Map map, final CommandInput cmd) {

        String color = cmd.getColor();
        String smell = cmd.getSmell();
        String sound = cmd.getSound();
        boolean hasColor = !color.equals("none");
        boolean hasSmell = !smell.equals("none");
        boolean hasSound = !sound.equals("none");

        Position pos = robot.getPosition();
        Cell cell = map.getCell(pos.getX(), pos.getY());
        ObjectNode result = MAPPER.createObjectNode();

        if (!hasColor && !hasSmell && !hasSound) {
            if (cell.getWater() != null) {
                result.put("message", "The scanned object is water.");
                cell.getWater().setScanned(true);
                robot.setEnergyStatus(robot.getEnergyStatus() - ENERGY_REQUIRED);
                cell.getWater().setScannedTimestamp(cmd.getTimestamp());
            } else {
                result.put("message", "ERROR: Object not found. Cannot perform action");
            }
            return result;
        }

        if (hasColor && hasSmell && !hasSound) {
            if (cell.getPlant() != null) {
                result.put("message", "The scanned object is a plant.");
                cell.getPlant().setScanned(true);
                robot.setEnergyStatus(robot.getEnergyStatus() - ENERGY_REQUIRED);
                cell.getPlant().setScannedTimestamp(cmd.getTimestamp());
            } else {
                result.put("message", "ERROR: Object not found. Cannot perform action");
            }
            return result;
        }

        if (hasColor && hasSmell && hasSound) {
            if (cell.getAnimal() != null) {
                result.put("message", "The scanned object is an animal.");
                cell.getAnimal().setScanned(true);
                robot.setEnergyStatus(robot.getEnergyStatus() - ENERGY_REQUIRED);
                cell.getAnimal().setScannedTimestamp(cmd.getTimestamp());
            } else {
                result.put("message", "ERROR: Object not found. Cannot perform action");
            }
            return result;
        }

        result.put("message", "ERROR: Object not found. Cannot perform action");
        return result;
    }
}
