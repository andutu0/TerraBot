package terrabot.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import terrabot.entities.Air.Air;
import terrabot.entities.Soil.Soil;
import terrabot.map.Cell;
import terrabot.map.Map;
import terrabot.simulation.Simulation;

public final class MapOutput {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private MapOutput() { }

    /**
     * Builds the JSON array describing the state of the entire map.
     * @param map the map containing all cells of the territory;
     * @param sim the active simulation providing the quality evaluation logic;
     * @return an ArrayNode representing the final JSON structure required
     */
    public static ArrayNode build(final Map map, final Simulation sim) {
        ArrayNode arr = MAPPER.createArrayNode();

        int width = map.getColumns();
        int height = map.getRows();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = map.getCell(x, y);

                ObjectNode cellNode = MAPPER.createObjectNode();

                ArrayNode section = MAPPER.createArrayNode();
                section.add(x);
                section.add(y);
                cellNode.set("section", section);

                int total = 0;
                if (cell.getPlant() != null) {
                    total++;
                }
                if (cell.getAnimal() != null) {
                    total++;
                }
                if (cell.getWater() != null) {
                    total++;
                }
                cellNode.put("totalNrOfObjects", total);

                Soil soil = cell.getSoil();
                String soilLabel = "poor";
                if (soil != null) {
                    double sq = soil.computeSoilQuality();
                    soilLabel = sim.qualityLabel(sq);
                }
                cellNode.put("soilQuality", soilLabel);

                Air air = cell.getAir();
                String airLabel = "poor";
                if (air != null) {
                    double aq = air.computeAirQuality();
                    airLabel = sim.qualityLabel(aq);
                }
                cellNode.put("airQuality", airLabel);

                arr.add(cellNode);
            }
        }

        return arr;
    }
}
