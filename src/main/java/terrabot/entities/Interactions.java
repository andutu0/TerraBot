package terrabot.entities;

import terrabot.entities.Animal.Animal;
import terrabot.map.Cell;
import terrabot.map.Map;
import terrabot.simulation.Simulation;

public final class Interactions {
    private static final double PLANT_GROWTH_RATE = 0.2;
    private static final double WATER_INTERACTION = 0.1;
    private static final double WATER_INTAKE_RATE = 0.08;

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
                if (cell.getWater() != null) {
                    if (cell.getWater().isScanned()) {
                        if ((sim.getCurrTime() - cell.getWater().getScannedTimestamp()) % 2 == 0
                            && (sim.getCurrTime() - cell.getWater().getScannedTimestamp()) != 0) {
                            if (cell.getSoil() != null) {
                                double currentWR = cell.getSoil().getWaterRetention();
                                cell.getSoil().setWaterRetention(currentWR + WATER_INTERACTION);
                            }
                            if (cell.getAir() != null) {
                                double currentHum = cell.getAir().getHumidity();
                                cell.getAir().setHumidity(currentHum + WATER_INTERACTION);
                                cell.getAir().computeAirQuality();
                            }
                        }
                    }
                }

                if (cell.getPlant() != null) {
                    if (cell.getPlant().isScanned() && cell.getSoil() != null) {
                        cell.getPlant().setPlantGrowth(PLANT_GROWTH_RATE);
                    }
                    if (cell.getPlant().isScanned() && cell.getWater() != null) {
                        cell.getPlant().setPlantGrowth(PLANT_GROWTH_RATE);
                    }
                    if (cell.getPlant().isScanned() && cell.getAir() != null) {
                        double currentOxygen = cell.getAir().getOxygenLevel();
                        cell.getAir().setOxygenLevel(currentOxygen
                                + cell.getPlant().releaseOxygen());
                        cell.getAir().computeAirQuality();
                    }
                }

                if (cell.getAnimal() != null) {
                    if (cell.getAnimal().isScanned()) {
                        if (cell.getAir() != null) {
                            if (cell.getAir().isToxic()) {
                                cell.getAnimal().setAnimalStatus("Sick");
                            }
                        }
                        moveAnimal(map, sim, x, y);
                    }
                }
            }
        }
    }

    /**
     * Moves an animal according to the specified rules
     * Once every 2 iterations
     */
    private static void moveAnimal(final Map map, final Simulation sim,
                                   final int x, final int y) {
        final int neighboursCnt = 4;
        final int[] dx = {0, 1, 0, -1};
        final int[] dy = {1, 0, -1, 0};
        Cell cell = map.getCell(x, y);
        Animal animal = cell.getAnimal();
        int width = map.getColumns();
        int height = map.getRows();
        int moveX;
        int moveY;

        if (!((sim.getCurrTime() - cell.getAnimal().getScannedTimestamp()) % 2 == 0
                && (sim.getCurrTime() - cell.getAnimal().getScannedTimestamp()) != 0)) {
            return;
        }

        // first we check if we have any neighbour (we should have it)
        boolean hasNeighbour = false;
        int firstNeighborX = 0, firstNeighborY = 0;

        // then we check if we have both plant and water on that cell
        boolean hasPlantAndWater = false;
        int bestPlantWaterRow = 0, bestPlantWaterCol = 0;
        int bestPlantWater = -999;

        // if we don't have plant + water, we check for plant first
        boolean hasPlant = false;
        int firstPlantX = 0, firstPlantY = 0;

        // lastly we check for the best water possible
        boolean hasWater = false;
        int bestWaterX = 0, bestWaterY = 0;
        int bestWaterQuality = -999;

        for (int i = 0; i < neighboursCnt; i++) {
            int searchX = x + dx[i];
            int searchY = y + dy[i];

            if (searchX < 0 || searchX >= width || searchY < 0 || searchY >= height) {
                continue;
            }

            Cell neighbour = map.getCell(searchX, searchY);

            if (!hasNeighbour) {
                hasNeighbour = true;
                firstNeighborX = searchX;
                firstNeighborY = searchY;
            }

            boolean hasPlantHere = neighbour.getPlant() != null;
            boolean hasWaterHere = neighbour.getWater() != null;

            if (hasPlantHere && hasWaterHere) {
                int quality = neighbour.getWater().getWaterQuality();
                if (!hasPlantAndWater || quality > bestPlantWater) {
                    hasPlantAndWater = true;
                    bestPlantWater = quality;
                    bestPlantWaterRow = searchX;
                    bestPlantWaterCol = searchY;
                }
            }

            if (hasPlantHere && !hasPlant) {
                hasPlant = true;
                firstPlantX = searchX;
                firstPlantY = searchY;
            }

            if (hasWaterHere) {
                int quality = neighbour.getWater().getWaterQuality();
                if (!hasWater || quality > bestWaterQuality) {
                    hasWater = true;
                    bestWaterQuality = quality;
                    bestWaterX = searchX;
                    bestWaterY = searchY;
                }
            }
        }

        if (hasPlantAndWater) {
            moveX = bestPlantWaterRow;
            moveY = bestPlantWaterCol;
        } else if (hasPlant) {
            moveX = firstPlantX;
            moveY = firstPlantY;
        } else if (hasWater) {
            moveX = bestWaterX;
            moveY = bestWaterY;
        } else if (hasNeighbour) {
            moveX = firstNeighborX;
            moveY = firstNeighborY;
        } else {
            return;
        }

        Cell targetCell = map.getCell(moveX, moveY);
        targetCell.setAnimal(animal);
        cell.setAnimal(null);
    }
}
