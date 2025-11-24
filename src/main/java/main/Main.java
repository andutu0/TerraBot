package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import fileio.InputLoader;
import fileio.SimulationInput;
import terrabot.simulation.Simulation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {

    private Main() {
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectWriter WRITER = MAPPER.writer().withDefaultPrettyPrinter();

    /**
     * @param inputPath input file path
     * @param outputPath output file path
     * @throws IOException when files cannot be loaded.
     */
    public static void action(final String inputPath,
                              final String outputPath) throws IOException {
        InputLoader inputLoader = new InputLoader(inputPath);
        ArrayNode output = MAPPER.createArrayNode();

        List<Simulation> simulations = new ArrayList<>();
        for (SimulationInput simInput : inputLoader.getSimulations()) {
            simulations.add(new Simulation(simInput));
        }

        int currentSimulationIndex = -1;
        Simulation currentSimulation = null;

        for (CommandInput cmd : inputLoader.getCommands()) {
            if ("startSimulation".equals(cmd.getCommand())) {
                ++currentSimulationIndex;
                if (currentSimulationIndex >= simulations.size()) {
                    ObjectNode node = MAPPER.createObjectNode();
                    node.put("command", cmd.getCommand());
                    node.put("message",
                            "ERROR: Simulation already started. Cannot perform action");
                    node.put("timestamp", cmd.getTimestamp());
                    output.add(node);
                    continue;
                }
                currentSimulation = simulations.get(currentSimulationIndex);
            }
            if (currentSimulation == null) {
                ObjectNode node = MAPPER.createObjectNode();
                node.put("command", cmd.getCommand());
                node.put("message",
                        "ERROR: Simulation not started. Cannot perform action");
                node.put("timestamp", cmd.getTimestamp());
                output.add(node);
            } else {
                ObjectNode cmdOut = currentSimulation.processCommand(cmd);
                output.add(cmdOut);
            }
        }

        File outputFile = new File(outputPath);
        WRITER.writeValue(outputFile, output);
    }
}
