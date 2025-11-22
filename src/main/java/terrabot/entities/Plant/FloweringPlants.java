package terrabot.entities.Plant;
import fileio.PlantInput;

public class FloweringPlants extends Plant {
    private static final double STUCK_CHANCE = 0.9;

    public FloweringPlants(final PlantInput input) {
        super(input);
    }

    /**
     * Computes the probability of Flowering Plants to get the robot stuck.
     *
     * @return the computed stuck chance
     */
    public double getStuckChance() {
        return STUCK_CHANCE;
    }
}
