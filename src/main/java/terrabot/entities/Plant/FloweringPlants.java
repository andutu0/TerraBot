package terrabot.entities.Plant;
import fileio.PlantInput;

public class FloweringPlants extends Plant {
    private static final double STUCK_CHANCE = 0.9;
    private static final double RELEASE_AMOUNT = 6;

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

    /**
     * Releases oxygen depending on the plant's age.
     * @return the amount of oxygen released
     */
    @Override
    public final double releaseOxygen() {
        return computeReleaseOxygen(RELEASE_AMOUNT);
    }
}
