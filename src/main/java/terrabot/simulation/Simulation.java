package terrabot.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.SimulationInput;
import lombok.Getter;
import terrabot.commands.PrintEnvConditions;
import terrabot.commands.MapOutput;
import terrabot.commands.MoveRobot;
import terrabot.entities.Position;
import terrabot.map.Map;
import terrabot.map.MapInit;
import terrabot.TerraBot;

public final class Simulation {
    private static final int GOOD_QUALITY_THRESHOLD = 70;
    private static final int MODERATE_QUALITY_THRESHOLD = 40;

    @Getter
    private final Map map;
    @Getter
    private final TerraBot robot;
    private boolean started = false;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Simulation(final SimulationInput simulationInput) {
        this.map = MapInit.build(simulationInput);
        this.robot = new TerraBot(new Position(0, 0), simulationInput.getEnergyPoints());
    }

    /**
     * Processes a command and returns the result.
     *
     * @param cmd the command to process
     * @return ObjectNode containing the command response
     */
    public ObjectNode processCommand(final CommandInput cmd) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("command", cmd.getCommand());
        node.put("timestamp", cmd.getTimestamp());

        switch (cmd.getCommand()) {
            case "startSimulation" -> {
                if (started) {
                    node.put("message", "ERROR: Simulation already started. Cannot perform action");
                } else {
                    started = true;
                    node.put("message", "Simulation has started.");
                }
            }
            case "endSimulation" -> {
                if (!started) {
                    node.put("message", "ERROR: Simulation not started. Cannot perform action");
                } else {
                    started = false;
                    node.put("message", "Simulation has ended.");
                }
            }
            case "printEnvConditions" -> {
                if (!started) {
                    node.put("message", "ERROR: Simulation not started. Cannot perform action");
                } else {
                    ObjectNode output = PrintEnvConditions.build(map, robot);
                    node.set("output", output);
                }
            }
            case "printMap" -> {
                if (!started) {
                    node.put("message", "ERROR: Simulation not started. Cannot perform action");
                } else {
                    ArrayNode output = MapOutput.build(map, this);
                    node.set("output", output);
                }
            }
            case "moveRobot" -> {
                if (!started) {
                    node.put("message", "ERROR: Simulation not started. Cannot perform action");
                } else {
                    ObjectNode moveNode = MoveRobot.move(robot, map);
                    node.put("message", moveNode.get("message").asText());
                }
            }
            default -> {
                node.put("message", "ERROR: Unknown command");
            }
        }

        return node;
    }

    /**
     * Calculates the quality label based on a score.
     * @param score the quality score to evaluate
     * @return String representing the quality label ("good", "moderate", or "poor")
     */
    public String qualityLabel(final double score) {
        if (score >= GOOD_QUALITY_THRESHOLD) {
            return "good";
        }
        if (score >= MODERATE_QUALITY_THRESHOLD) {
            return "moderate";
        }
        return "poor";
    }
}
