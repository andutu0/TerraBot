package terrabot.entities.Plant;
import fileio.PlantInput;

public class GymnospermsPlants extends Plant {
    private static final double STUCK_CHANCE = 0.6;
    private static final double RELEASE_AMOUNT = 0;

    public GymnospermsPlants(final PlantInput input) {
        super(input);
    }

    /**
     * Computes the probability of Gymnosperms to get the robot stuck.
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
