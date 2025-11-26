package terrabot.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import terrabot.TerraBot;

public final class PrintKnowledgeBase {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private PrintKnowledgeBase() { }

    /**
     * Builds a JSON representation of the robot's knowledge base.
     *
     * @param robot the TerraBot whose knowledge base is being printed
     * @return an ArrayNode containing all topics and their associated facts
     */
    public static ArrayNode build(final TerraBot robot) {
        ArrayNode output = MAPPER.createArrayNode();

        robot.getKnowledgeBase().forEach((topic, facts) -> {
            ObjectNode topicNode = MAPPER.createObjectNode();
            topicNode.put("topic", topic);

            ArrayNode factsArray = MAPPER.createArrayNode();
            facts.forEach(factsArray::add);

            topicNode.set("facts", factsArray);
            output.add(topicNode);
        });

        return output;
    }
}
