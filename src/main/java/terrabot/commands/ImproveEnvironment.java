// java
package terrabot.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.CommandInput;
import terrabot.TerraBot;
import terrabot.map.Cell;
import terrabot.map.SimMap;

public final class ImproveEnvironment {
    private static final int ENERGY_COST = 10;
    private static final double OXYGEN_INCREASE = 0.3;
    private static final double ORGANIC_MATTER_INCREASE = 0.3;
    private static final double HUMIDITY_INCREASE = 0.2;
    private static final double MOISTURE_INCREASE = 0.2;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ImproveEnvironment() { }

    /**
     * Applies an improvement at the robot's current position.
     *
     * @param robot the TerraBot performing the improvement
     * @param simMap the simulation map
     * @param cmd the command specifying improvement type and component name
     * @return an ObjectNode containing the result message of the operation
     */
    public static ObjectNode improve(final TerraBot robot, final SimMap simMap,
                                     final CommandInput cmd) {
        ObjectNode node = MAPPER.createObjectNode();

        if (robot.getEnergyStatus() < ENERGY_COST) {
            node.put("message",
                    "ERROR: Not enough battery left. Cannot perform action");
            return node;
        }

        final String improvementType = cmd.getImprovementType();
        final String componentName = cmd.getName();

        if (!robot.getInventory().containsKey(componentName)) {
            node.put("message",
                    "ERROR: Subject not yet saved. Cannot perform action");
            return node;
        }

        if (!robot.getKnowledgeBase().containsKey(componentName)) {
            node.put("message",
                    "ERROR: Fact not yet saved. Cannot perform action");
            return node;
        }

        final Cell cell = simMap.getCell(robot.getPosition().getX(),
                robot.getPosition().getY());

        robot.setEnergyStatus(robot.getEnergyStatus() - ENERGY_COST);
        final String message = applyImprovement(cell, improvementType, componentName);
        node.put("message", message);
        return node;
    }

    private static String applyImprovement(final Cell cell,
                                           final String improvementType,
                                           final String componentName) {
        return switch (improvementType) {
            case "plantVegetation" -> {
                if (cell.getAir() != null) {
                    double current = cell.getAir().getOxygenLevel();
                    cell.getAir().setOxygenLevel(current + OXYGEN_INCREASE);
                    cell.getAir().computeAirQuality();
                }
                yield "The " + componentName + " was planted successfully.";
            }
            case "fertilizeSoil" -> {
                if (cell.getSoil() != null) {
                    double current = cell.getSoil().getOrganicMatter();
                    cell.getSoil().setOrganicMatter(current + ORGANIC_MATTER_INCREASE);
                }
                yield "The soil was successfully fertilized using " + componentName;
            }
            case "increaseHumidity" -> {
                if (cell.getAir() != null) {
                    double current = cell.getAir().getHumidity();
                    cell.getAir().setHumidity(current + HUMIDITY_INCREASE);
                    cell.getAir().computeAirQuality();
                }
                yield "The humidity was successfully increased using " + componentName;
            }
            case "increaseMoisture" -> {
                if (cell.getSoil() != null) {
                    double current = cell.getSoil().getWaterRetention();
                    cell.getSoil().setWaterRetention(current + MOISTURE_INCREASE);
                }
                yield "The moisture was successfully increased using " + componentName;
            }
            default -> "ERROR: Unknown improvement type";
        };
    }

    private static String getRequiredFactSubject(final String improvementType,
                                                 final String name) {
        return switch (improvementType) {
            case "plantVegetation" -> "Method to plant " + name;
            case "fertilizeSoil" -> "Method to fertilize soil using " + name;
            case "increaseHumidity" -> "Method to increase humidity using " + name;
            case "increaseMoisture" -> "Method to increase moisture using " + name;
            default -> "";
        };
    }
}
