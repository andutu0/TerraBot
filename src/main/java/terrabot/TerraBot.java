package terrabot;

import lombok.Getter;
import lombok.Setter;
import terrabot.entities.Position;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TerraBot {

    @Getter @Setter
    private Position position;
    @Getter @Setter
    private int energyStatus;
    @Getter
    private final Map<String, List<String>> knowledgeBase = new LinkedHashMap<>();
    @Getter
    private final Map<String, Integer> inventory = new LinkedHashMap<>();

    public TerraBot(final Position startPos, final int energyStatus) {
        this.position = startPos;
        this.energyStatus = energyStatus;
    }

    /**
     * Adds a scanned entity to the inventory.
     * @param entityName the name of the scanned entity (e.g., "Deer", "Rose")
     */
    public void addToInventory(final String entityName) {
        inventory.put(entityName, inventory.getOrDefault(entityName, 0) + 1);
    }

    /**
     * Removes an entity from the inventory.
     * @param entityName the name of the entity to remove
     * @return true if successful, false if entity is not found
     */
    public boolean removeFromInventory(final String entityName) {
        if (!inventory.containsKey(entityName)) {
            return false;
        }

        int count = inventory.get(entityName);
        if (count > 1) {
            inventory.put(entityName, count - 1);
        } else {
            inventory.remove(entityName);
        }
        return true;
    }

    /**
     * Checks if inventory contains a specific entity.
     * @param entityName the entity name to check
     * @return true if entity exists in inventory, false otherwise
     */
    public boolean hasInInventory(final String entityName) {
        return inventory.containsKey(entityName);
    }

    /**
     * Adds a fact to the knowledge base.
     * @param name the component name
     * @param fact the fact to add
     */
    public void addFact(final String name, final String fact) {
        knowledgeBase.computeIfAbsent(name, k -> new ArrayList<>()).add(fact);
    }
}
