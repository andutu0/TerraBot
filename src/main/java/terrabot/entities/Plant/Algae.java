package terrabot.entities.Plant;
import fileio.PlantInput;

public class Algae extends Plant {
    private static final double STUCK_CHANCE = 0.2;
    private static final double RELEASE_AMOUNT = 0.5;

    private static final double YOUNG = 0.2;
    private static final double MATURE = 0.7;
    private static final double OLD = 0.4;

    private static final int AGE_YOUNG = 1;
    private static final int AGE_MATURE = 2;
    private static final int AGE_OLD = 3;


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
        int age = this.getPlantAge();

        if (age == AGE_YOUNG) {
            return RELEASE_AMOUNT + YOUNG;
        } else if (age == AGE_MATURE) {
            return RELEASE_AMOUNT + MATURE;
        } else if (age == AGE_OLD) {
            return RELEASE_AMOUNT + OLD;
        }
        return 0;
    }
}
