package terrabot.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import terrabot.TerraBot;

public final class LearnFact {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private LearnFact() { }

    /**
     * Learns a new fact by adding it to the robot's knowledge base.
     *
     * @param robot the TerraBot learning the new fact
     * @param cmd the command containing the component name and fact subject
     * @return an ObjectNode containing the result message
     */
    public static ObjectNode learn(final TerraBot robot, final CommandInput cmd) {
        ObjectNode result = MAPPER.createObjectNode();
        String components = cmd.getComponents();
        String subject = cmd.getSubject();

        if (robot.getEnergyStatus() < 2) {
            result.put("message", "ERROR: Not enough battery left. Cannot perform action");
            return result;
        }

        if (!robot.getInventory().containsKey(components)) {
            result.put("message", "ERROR: Subject not yet saved. Cannot perform action");
            return result;
        }

        // Add fact to knowledge base
        robot.addFact(components, subject);
        result.put("message", "The fact has been successfully saved in the database.");
        robot.setEnergyStatus(robot.getEnergyStatus() - 2);
        return result;
    }
}
