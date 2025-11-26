package terrabot.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.SimulationInput;
import lombok.Getter;
import lombok.Setter;
import terrabot.commands.ChangeWeatherConditions;
import terrabot.commands.ImproveEnvironment;
import terrabot.commands.LearnFact;
import terrabot.commands.MapOutput;
import terrabot.commands.MoveRobot;
import terrabot.commands.PrintEnvConditions;
import terrabot.commands.PrintKnowledgeBase;
import terrabot.commands.ScanObject;
import terrabot.entities.Air.Air;
import terrabot.entities.Interactions;
import terrabot.entities.Position;
import terrabot.map.Cell;
import terrabot.map.SimMap;
import terrabot.map.MapInit;
import terrabot.TerraBot;

public final class Simulation {
    private static final int GOOD_QUALITY_THRESHOLD = 70;
    private static final int MODERATE_QUALITY_THRESHOLD = 40;

    @Getter @Setter
    private boolean weatherActive = false;
    @Getter @Setter
    private String activeWeatherType = null;
    @Getter @Setter
    private int weatherRevertAt = 0;
    @Getter
    private int currTime = 0;

    @Getter
    private final SimMap simMap;
    @Getter
    private final TerraBot robot;
    private boolean started = false;
    private int chargeUntil = 0;
    private boolean charging = false;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Simulation(final SimulationInput simulationInput) {
        this.simMap = MapInit.build(simulationInput);
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
        maybeRevertWeather(cmd.getTimestamp());

        if (started) {
            ++currTime;
            Interactions.interact(simMap, this);
        }

        advanceEnvironment(cmd.getTimestamp());

        if (charging && cmd.getTimestamp() < chargeUntil) {
            node.put("message", "ERROR: Robot still charging. Cannot perform action");
            node.put("timestamp", cmd.getTimestamp());
            return node;
        } else {
            switch (cmd.getCommand()) {
                case "startSimulation" -> {
                    if (started) {
                        node.put("message",
                                "ERROR: Simulation already started. Cannot perform action");
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
                        ObjectNode output = PrintEnvConditions.build(simMap, robot, this);
                        node.set("output", output);
                    }
                }
                case "printMap" -> {
                    if (!started) {
                        node.put("message", "ERROR: Simulation not started. Cannot perform action");
                    } else {
                        ArrayNode output = MapOutput.build(simMap, this);
                        node.set("output", output);
                    }
                }
                case "moveRobot" -> {
                    if (!started) {
                        node.put("message", "ERROR: Simulation not started. Cannot perform action");
                    } else {
                        ObjectNode moveNode = MoveRobot.move(robot, simMap);
                        node.put("message", moveNode.get("message").asText());
                    }
                }
                case "getEnergyStatus" -> {
                    if (!started) {
                        node.put("message", "ERROR: Simulation not started. Cannot perform action");
                    } else {
                        node.put("message", "TerraBot has "
                                + robot.getEnergyStatus() + " energy points left.");
                    }
                }
                case "rechargeBattery" -> {
                    if (!started) {
                        node.put("message", "ERROR: Simulation not started. Cannot perform action");
                    } else {
                        charging = true;
                        int amountToCharge = cmd.getTimeToCharge();
                        chargeUntil = cmd.getTimestamp() +  amountToCharge;
                        robot.setEnergyStatus(robot.getEnergyStatus() + amountToCharge);
                        node.put("message", "Robot battery is charging.");
                    }
                }
                case "changeWeatherConditions" -> {
                    if (!started) {
                        node.put("message", "ERROR: Simulation not started. Cannot perform action");
                    } else {
                        boolean changed = ChangeWeatherConditions.apply(cmd, simMap, this);
                        if (changed) {
                            node.put("message", "The weather has changed.");
                        } else {
                            node.put("message",
                                    "ERROR: The weather change does not affect the environment. "
                                            + "Cannot perform action");
                        }
                    }
                }
                case "scanObject" -> {
                    if (!started) {
                        node.put("message", "ERROR: Simulation not started. Cannot perform action");
                    } else {
                        ObjectNode scanNode = ScanObject.scan(robot, simMap, cmd);
                        node.put("message", scanNode.get("message").asText());
                    }
                }
                case "learnFact" -> {
                    if (!started) {
                        node.put("message", "ERROR: Simulation not started. Cannot perform action");
                    } else {
                        ObjectNode scanNode = LearnFact.learn(robot, cmd);
                        node.put("message", scanNode.get("message").asText());
                    }
                }
                case "printKnowledgeBase" -> {
                    if (!started) {
                        node.put("message", "ERROR: Simulation not started. Cannot perform action");
                    } else {
                        ArrayNode output = PrintKnowledgeBase.build(robot);
                        node.set("output", output);
                    }
                }
                case "improveEnvironment" -> {
                    if (!started) {
                        node.put("message", "ERROR: Simulation not started. Cannot perform action");
                    } else {
                        ObjectNode improveNode = ImproveEnvironment.improve(robot, simMap, cmd);
                        node.put("message", improveNode.get("message").asText());
                    }
                }
                default -> {
                    node.put("message", "ERROR: Unknown command");
                }
            }
        }

        node.put("timestamp", cmd.getTimestamp());
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

    // checks if the 2 timestamp weather duration passed or not
    // if it passed reverts the weather effects on the map
    private void maybeRevertWeather(final int currentTimestamp) {
        if (!weatherActive || currentTimestamp < weatherRevertAt) {
            return;
        }

        for (int y = 0; y < simMap.getRows(); y++) {
            for (int x = 0; x < simMap.getColumns(); x++) {
                Cell cell = simMap.getCell(x, y);
                Air air = cell.getAir();
                if (air == null) {
                    continue;
                }
                if (isAffectedByWeather(air, activeWeatherType)) {
                    air.computeAirQuality();
                }
            }
        }

        weatherActive = false;
        activeWeatherType = null;
    }

    /**
     * Checks if a specific air cell is affected by the current weather.
     * @param air the specific air cell
     * @param weatherType the current weather
     * @return boolean if the current cell is affected
     */
    public static boolean isAffectedByWeather(final Air air, final String weatherType) {
        final String type = air.getType();

        return switch (weatherType) {
            case "desertStorm" -> type.equals("DesertAir");
            case "peopleHiking" -> type.equals("MountainAir");
            case "newSeason" -> type.equals("TemperateAir");
            case "polarStorm" -> type.equals("PolarAir");
            case "rainfall" -> type.equals("TropicalAir");
            default -> false;
        };
    }

    // if the commands are not in consecutive timestamps,
    // we still need to do the interactions for the time in between
    private void advanceEnvironment(final int targetTimestamp) {
        if (!started) {
            currTime = targetTimestamp;
            return;
        }
        if (targetTimestamp <= currTime) {
            return;
        }
        for (int time = currTime; time < targetTimestamp; time++) {
            Interactions.interact(simMap, this);
        }
        currTime = targetTimestamp;
    }
}
