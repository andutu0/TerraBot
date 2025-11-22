package terrabot.map;

import fileio.SimulationInput;
import fileio.TerritorySectionParamsInput;
import fileio.SoilInput;
import fileio.WaterInput;
import fileio.AirInput;
import fileio.PlantInput;
import fileio.AnimalInput;
import fileio.PairInput;
import terrabot.entities.Air.Air;
import terrabot.entities.Air.AirGenerator;
import terrabot.entities.Animal.AnimalGenerator;
import terrabot.entities.Plant.PlantGenerator;
import terrabot.entities.Soil.Soil;
import terrabot.entities.Soil.SoilGenerator;
import terrabot.entities.Animal.Animal;
import terrabot.entities.Plant.Plant;
import terrabot.entities.Water.Water;
import terrabot.entities.Position;

public final class MapInit {

    private MapInit() { }

    /**
     * Builds and initializes a Map from the given simulation input.
     * Processes territory dimensions and populates the map with soil, water,
     * air, plants, and animals based on the input configuration.
     *
     * @param sim the simulation input containing territory configuration
     * @return a fully initialized Map object with all entities placed
     */
    public static Map build(final SimulationInput sim) {
        String[] parts = sim.getTerritoryDim().split("x");
        int rows = Integer.parseInt(parts[0]);
        int cols = Integer.parseInt(parts[1]);
        Map world = new Map(rows, cols);

        TerritorySectionParamsInput territory = sim.getTerritorySectionParams();

        for (AirInput airInput : territory.getAir()) {
            for (PairInput p : airInput.getSections()) {
                Air air = AirGenerator.fromInput(airInput);

                Position pos = new Position(p.getX(), p.getY());
                air.setPosition(pos);

                world.getCell(pos.getX(), pos.getY()).setAir(air);
            }
        }

        for (SoilInput soilInput : territory.getSoil()) {
            for (PairInput p : soilInput.getSections()) {
                Soil soil = SoilGenerator.fromInput(soilInput);

                Position pos = new Position(p.getX(), p.getY());
                soil.setPosition(pos);

                world.getCell(pos.getX(), pos.getY()).setSoil(soil);
            }
        }

        for (WaterInput waterInput : territory.getWater()) {
            for (PairInput p : waterInput.getSections()) {
                Water w = new Water(waterInput);

                Position pos = new Position(p.getX(), p.getY());
                w.setPosition(pos);

                world.getCell(pos.getX(), pos.getY()).setWater(w);
            }
        }

        for (PlantInput plantInput : territory.getPlants()) {
            for (PairInput p : plantInput.getSections()) {
                Plant plant = PlantGenerator.fromInput(plantInput);

                Position pos = new Position(p.getX(), p.getY());
                plant.setPosition(pos);

                world.getCell(pos.getX(), pos.getY()).setPlant(plant);
            }
        }

        for (AnimalInput animalInput : territory.getAnimals()) {
            for (PairInput p : animalInput.getSections()) {
                Animal animal = AnimalGenerator.fromInput(animalInput);

                Position pos = new Position(p.getX(), p.getY());
                animal.setPosition(pos);

                world.getCell(pos.getX(), pos.getY()).setAnimal(animal);
            }
        }

        return world;
    }
}
