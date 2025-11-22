package terrabot.entities.Plant;

import fileio.PlantInput;

public final class PlantGenerator {

    private PlantGenerator() {
    }

    /**
     * Creates a Plant entity from the given PlantInput.
     *
     * @param input the input object containing plant parameters
     * @return a specific plant type
     * @throws IllegalArgumentException if the plant type is unknown
     */
    public static Plant fromInput(final PlantInput input) {
        return switch (input.getType()) {
            case "GymnospermsPlants"  -> new GymnospermsPlants(input);
            case "Algae" -> new Algae(input);
            case "Ferns"  -> new Ferns(input);
            case "FloweringPlants"  -> new FloweringPlants(input);
            case "Mosses"  -> new Mosses(input);
            default -> throw new IllegalArgumentException(
                    "Unknown plant type: " + input.getType()
            );
        };
    }
}
