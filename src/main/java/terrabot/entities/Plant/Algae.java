package terrabot.entities.Plant;
import fileio.PlantInput;

public class Algae extends Plant {
    private static final double STUCK_CHANCE = 0.2;
    private static final double RELEASE_AMOUNT = 0.5;

    public Algae(final PlantInput input) {
        super(input);
    }

    /**
     * Computes the probability of Algae to get the robot stuck.
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
