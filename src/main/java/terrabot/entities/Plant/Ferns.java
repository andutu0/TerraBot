package terrabot.entities.Plant;
import fileio.PlantInput;

public class Ferns extends Plant {
    private static final double STUCK_CHANCE = 0.3;
    private static final double RELEASE_AMOUNT = 0;

    public Ferns(final PlantInput input) {
        super(input);
    }

    /**
     * Computes the probability of Ferns to get the robot stuck.
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
