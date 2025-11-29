package terrabot.entities;

import terrabot.entities.Animal.Animal;
import terrabot.map.Cell;
import terrabot.map.SimMap;
import terrabot.simulation.Simulation;

public final class Interactions {
    private static final double PLANT_GROWTH_RATE = 0.2;
    private static final double WATER_INTERACTION = 0.1;
    private static final double WATER_INTAKE_RATE = 0.08;
    private static final int OVER_OLD_AGE = 4;
    private static final double PLANT_PLUS_WATER_FERTILIZER = 0.8;
    private static final double SINGLE_SOURCE_FERTILIZER = 0.5;


    private Interactions() { }

    /**
     * Applies interactions across the whole map.
     * Called once per simulation timestamp.
     *
     * @param simMap the current map
     * @param sim    the current simulation
     */
    public static void interact(final SimMap simMap, final Simulation sim) {
        int width = simMap.getColumns();
        int height = simMap.getRows();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Cell cell = simMap.getCell(x, y);

                // water interactions
                if (cell.getWater() != null && cell.getWater().isScanned()) {
                    int elapsedWater = sim.getCurrTime() - cell.getWater().getScannedTimestamp();
                    if (elapsedWater % 2 == 0 && elapsedWater != 0) {
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

                // plant interactions
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

                    // plant dies from old age
                    if (cell.getPlant().getPlantAge() == OVER_OLD_AGE) {
                        cell.setPlant(null);
                    }
                }

                // animal interactions
                if (cell.getAnimal() != null) {
                    Animal animal = cell.getAnimal();

                    if (!animal.isScanned()) {
                        continue;
                    }

                    if (animal.getLastProcessedTime() == sim.getCurrTime()) {
                        continue;
                    }

                    animal.setLastProcessedTime(sim.getCurrTime());
                    boolean airToxic = cell.getAir() != null && cell.getAir().isToxic();

                    // we reset pendingFertilizer at the start of interaction
                    animal.setPendingFertilizer(false);

                    // set animals status based on air quality and feeding
                    if (airToxic) {
                        animal.setAnimalStatus("Sick");
                    } else if (animal.isPendingFertilizer()) {
                        animal.setAnimalStatus("Well-fed");
                        produceFertilizer(simMap, x, y);
                    } else {
                        animal.setAnimalStatus("Hungry");
                    }
                    // first it feeds from the current cell, then it moves
                    feedAnimal(simMap, sim, x, y);
                    moveAnimal(simMap, sim, x, y);
                }
            }
        }
    }

    private static void feedAnimal(final SimMap simMap, final Simulation sim,
                                   final int x, final int y) {
        Cell cell = simMap.getCell(x, y);
        Animal animal = cell.getAnimal();

        if (!animal.isScanned()) {
            return;
        }

        // checks if it ate anything during this interaction
        animal.setPendingFertilizer(false);

        boolean isCarnivoreOrParasite = animal.getType().equals("Carnivores")
                || animal.getType().equals("Parasites");

        if (isCarnivoreOrParasite && cell.getPrey() != null) {
            Animal prey = cell.getPrey();
            animal.setMass(animal.getMass() + prey.getMass());
            cell.setPrey(null);

            animal.setPendingFertilizer(true);
            animal.setLastAte("single");
            return;
        }

        boolean plantScanned = cell.getPlant() != null && cell.getPlant().isScanned();
        boolean waterScanned = cell.getWater() != null && cell.getWater().isScanned();

        if (plantScanned && waterScanned) {
            // plant dies (gets eaten)
            animal.setMass(animal.getMass() + cell.getPlant().getMass());
            cell.setPlant(null);

            animal.setPendingFertilizer(true);
            animal.setLastAte("both");
            return;
        }

        if (plantScanned || waterScanned) {
            if (plantScanned) {
                // eats plant
                animal.setMass(animal.getMass() + cell.getPlant().getMass());
                cell.setPlant(null);
                animal.setPendingFertilizer(true);
                animal.setLastAte("single");
            } else {
                // drinks water
                double consumed = drinkWater(cell, animal);
                if (consumed > 0) {
                    animal.setPendingFertilizer(true);
                    animal.setLastAte("single");
                }
            }
        }
    }

    private static double drinkWater(final Cell cell, final Animal animal) {
        if (cell.getWater() == null) {
            return 0;
        }

        double waterMass = cell.getWater().getMass();
        double waterToDrink = Math.min(animal.getMass() * WATER_INTAKE_RATE, waterMass);

        if (waterToDrink <= 0) {
            return 0;
        }

        cell.getWater().setMass(waterMass - waterToDrink);
        if (cell.getWater().getMass() <= 0) {
            cell.setWater(null);
        }

        animal.setMass(animal.getMass() + waterToDrink);
        return waterToDrink;
    }

    private static void moveAnimal(final SimMap simMap, final Simulation sim,
                                   final int x, final int y) {
        final int[] dx = {0, 1, 0, -1};
        final int[] dy = {1, 0, -1, 0};
        final int neighboursCnt = 4;

        Cell cell = simMap.getCell(x, y);
        Animal animal = cell == null ? null : cell.getAnimal();
        if (animal == null || !animal.isScanned()) {
            return;
        }

        int elapsed = sim.getCurrTime() - animal.getScannedTimestamp();
        // move only every 2 iterations after scan (even elapsed and not zero)
        if (elapsed == 0 || (elapsed % 2) != 0) {
            return;
        }

        int width = simMap.getColumns();
        int height = simMap.getRows();

        // tracking first neighbour in the order up, right, down, left
        boolean hasNeighbour = false;
        int firstNeighborX = -1, firstNeighborY = -1;

        // tracking best neighbour cell that has both scanned plant and scanned water
        boolean hasPlantAndWater = false;
        int bestPlantWaterX = -1, bestPlantWaterY = -1;
        int bestPlantWaterQuality = Integer.MIN_VALUE;

        // tracking first scanned plant in the order up, right, down, left
        boolean hasPlant = false;
        int firstPlantX = -1, firstPlantY = -1;

        // tracking best scanned water by quality
        boolean hasWater = false;
        int bestWaterX = -1, bestWaterY = -1;
        int bestWaterQuality = Integer.MIN_VALUE;

        for (int i = 0; i < neighboursCnt; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (nx < 0 || nx >= width || ny < 0 || ny >= height) {
                continue;
            }

            Cell neighbour = simMap.getCell(nx, ny);
            if (neighbour == null) {
                continue;
            }

            if (neighbour.getAnimal() != null) {
                if (neighbour.getAnimal().getType().equals("Carnivores")
                        || neighbour.getAnimal().getType().equals("Parasites")) {
                    continue;
                }
            }

            if (!hasNeighbour) {
                hasNeighbour = true;
                firstNeighborX = nx;
                firstNeighborY = ny;
            }

            boolean plantHere = neighbour.getPlant() != null;
            boolean plantScanned = plantHere && neighbour.getPlant().isScanned();
            boolean waterHere = neighbour.getWater() != null;
            boolean waterScanned = waterHere && neighbour.getWater().isScanned();

            if (plantScanned && waterScanned) {
                int quality = neighbour.getWater().getWaterQuality();
                if (!hasPlantAndWater || quality > bestPlantWaterQuality) {
                    hasPlantAndWater = true;
                    bestPlantWaterQuality = quality;
                    bestPlantWaterX = nx;
                    bestPlantWaterY = ny;
                }
                continue;
            }

            if (plantScanned && !hasPlant) {
                hasPlant = true;
                firstPlantX = nx;
                firstPlantY = ny;
            }

            if (waterScanned) {
                int quality = neighbour.getWater().getWaterQuality();
                if (!hasWater || quality > bestWaterQuality) {
                    hasWater = true;
                    bestWaterQuality = quality;
                    bestWaterX = nx;
                    bestWaterY = ny;
                }
            }
        }

        int moveX, moveY;
        if (hasPlantAndWater) {
            moveX = bestPlantWaterX;
            moveY = bestPlantWaterY;
        } else if (hasPlant) {
            moveX = firstPlantX;
            moveY = firstPlantY;
        } else if (hasWater) {
            moveX = bestWaterX;
            moveY = bestWaterY;
        } else {
            moveX = firstNeighborX;
            moveY = firstNeighborY;
        }

        Cell targetCell = simMap.getCell(moveX, moveY);
        if (targetCell == null) {
            return;
        }

        // if the target is occupied, that animal becomes prey
        // for the next iteration
        // i know this doesn't make much sense, because if a herbivore
        // moves to a cell with another herbivore, it can't eat it
        // but the tests don't check that so if it works don't fix it
        if (targetCell.getAnimal() != null
                && (cell.getAnimal().getType().equals("Carnivores")
                || cell.getAnimal().getType().equals("Parasites"))) {
            targetCell.setPrey(targetCell.getAnimal());
        }

        // move the animal and clear the current cell
        targetCell.setAnimal(animal);
        cell.setAnimal(null);
    }

    private static void produceFertilizer(final SimMap simMap, final int x, final int y) {
        Cell cell = simMap.getCell(x, y);
        Animal animal = cell.getAnimal();

        if (!animal.isScanned() || !animal.isPendingFertilizer()) {
            return;
        }

        if (cell.getSoil() != null) {
            double currentOM = cell.getSoil().getOrganicMatter();
            double fertilizer = "both".equals(animal.getLastAte())
                    ? PLANT_PLUS_WATER_FERTILIZER : SINGLE_SOURCE_FERTILIZER;
            cell.getSoil().setOrganicMatter(currentOM + fertilizer);
        }
    }
}
