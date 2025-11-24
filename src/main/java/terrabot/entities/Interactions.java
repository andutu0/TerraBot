package terrabot.entities;

import terrabot.map.Cell;
import terrabot.map.Map;
import terrabot.simulation.Simulation;

public final class Interactions {
    private static final double PLANT_GROWTH_RATE = 0.2;
    private Interactions() { }


    /**
     * Applies interactions across the whole map.
     * Called once per simulation timestamp.
     *
     * @param map the current map
     * @param sim the current simulation
     */
    public static void interact(final Map map, final Simulation sim) {
        int width = map.getColumns();
        int height = map.getRows();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Cell cell = map.getCell(x, y);

                if (cell.getPlant() != null) {
                    if (cell.getPlant().isScanned() && cell.getSoil() != null) {
                        double currentGrowth = cell.getPlant().getPlantGrowth();
                        cell.getPlant().setPlantGrowth(PLANT_GROWTH_RATE);
                    }
                    if (cell.getPlant().isScanned() && cell.getWater() != null) {
                        double currentGrowth = cell.getPlant().getPlantGrowth();
                        cell.getPlant().setPlantGrowth(PLANT_GROWTH_RATE);
                    }
                    if (cell.getPlant().isScanned() && cell.getAir() != null) {
                        double currentOxygen = cell.getAir().getOxygenLevel();
                        cell.getAir().setOxygenLevel(currentOxygen
                                + cell.getPlant().releaseOxygen());
                        cell.getAir().computeAirQuality();
                    }
                }
            }
        }
    }
}
