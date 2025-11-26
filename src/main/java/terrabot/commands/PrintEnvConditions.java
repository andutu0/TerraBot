package terrabot.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import terrabot.TerraBot;
import terrabot.entities.Air.Air;
import terrabot.entities.Soil.Soil;
import terrabot.entities.Plant.Plant;
import terrabot.entities.Animal.Animal;
import terrabot.entities.Water.Water;
import terrabot.entities.Position;
import terrabot.map.Cell;
import terrabot.map.SimMap;
import terrabot.simulation.Simulation;

public final class PrintEnvConditions {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private PrintEnvConditions() {
    }
    /**
     * Creates a JSON object describing the environmental conditions of the
     * current cell.
     * @param simMap the map containing all territory cells
     * @param bot the TerraBot whose current position is inspected
     * @return an ObjectNode containing all environment fields for that cell
     */
    public static ObjectNode build(final SimMap simMap, final TerraBot bot, final Simulation sim) {
        ObjectNode out = MAPPER.createObjectNode();

        Position pos = bot.getPosition();
        Cell cell = simMap.getCell(pos.getX(), pos.getY());

        Soil soil = cell.getSoil();
        if (soil != null) {
            ObjectNode soilNode = MAPPER.createObjectNode();
            soilNode.put("type", soil.getType());
            soilNode.put("name", soil.getName());
            soilNode.put("mass", soil.getMass());
            soilNode.put("nitrogen", soil.getNitrogen());
            soilNode.put("waterRetention", soil.getWaterRetention());
            soilNode.put("soilpH", soil.getSoilpH());
            soilNode.put("organicMatter", soil.getOrganicMatter());
            soil.addSpecificFields(soilNode);
            soilNode.put("soilQuality", soil.computeSoilQuality());
            out.set("soil", soilNode);
        }

        Plant plant = cell.getPlant();
        if (plant != null) {
            ObjectNode plantNode = MAPPER.createObjectNode();
            plantNode.put("type", plant.getType());
            plantNode.put("name", plant.getName());
            plantNode.put("mass", plant.getMass());
            out.set("plants", plantNode);
        }

        Animal animal = cell.getAnimal();
        if (animal != null) {
            ObjectNode animalNode = MAPPER.createObjectNode();
            animalNode.put("type", animal.getType());
            animalNode.put("name", animal.getName());
            animalNode.put("mass", animal.getMass());
            out.set("animals", animalNode);
        }

        Water water = cell.getWater();
        if (water != null) {
            ObjectNode waterNode = MAPPER.createObjectNode();
            waterNode.put("type", water.getType());
            waterNode.put("name", water.getName());
            waterNode.put("mass", water.getMass());
            out.set("water", waterNode);
        }

        Air air = cell.getAir();
        if (air != null) {
            ObjectNode airNode = MAPPER.createObjectNode();
            airNode.put("type", air.getType());
            airNode.put("name", air.getName());
            airNode.put("mass", air.getMass());
            airNode.put("humidity", air.getHumidity());
            airNode.put("temperature", air.getTemperature());
            airNode.put("oxygenLevel", air.getOxygenLevel());
            air.addSpecificFields(airNode);

            if (!sim.isWeatherActive()) {
                air.computeAirQuality();
            }

            airNode.put("airQuality", air.getAirQuality());
            out.set("air", airNode);
        }

        return out;
    }
}
