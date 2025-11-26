package terrabot.entities.Plant;

import fileio.PlantInput;
import lombok.Getter;
import lombok.Setter;
import terrabot.entities.Entity;

public abstract class Plant extends Entity {
    private static final double YOUNG = 0.2;
    private static final double MATURE = 0.7;
    private static final double OLD = 0.4;
    private static final int AGE_YOUNG = 1;
    private static final int AGE_MATURE = 2;
    private static final int AGE_OLD = 3;
    @Getter @Setter
    private String type;
    @Getter
    private double plantGrowth;
    @Getter @Setter
    private int plantAge;

    public Plant(final PlantInput input) {
        super(input.getName(), input.getMass());
        this.type = input.getType();
        plantGrowth = 0;
        plantAge = 1;
    }

    /**
     * Increments the plant's growth by a fixed amount.
     * Growth accumulates until it reaches 1.0, at which point
     * the growth resets and the plant ages by 1.
     * @param added the growth amount to add
     */
    public void setPlantGrowth(final double added) {
        this.plantGrowth += added;

        if (this.plantGrowth >= 1.0) {
            this.plantGrowth -= 1.0;
            ++this.plantAge;
        }
    }


    /**
     * Computes the probability of a specific type of plant to get the robot stuck.
     *
     * @return the computed stuck chance
     */
    public abstract double getStuckChance();

    /**
     * Computes the oxygen level each type of plant releases in the air.
     *
     * @return the oxygen amount released by a plant
     */
    public abstract double releaseOxygen();

    /**
     * helper for releaseOxygen to remove duplicate code
     * in the subclasses
     *
     * @param releaseAmount type-specific amount to add
     * @return computed oxygen release (non-negative)
     */
    protected double computeReleaseOxygen(final double releaseAmount) {
        if (plantAge == AGE_YOUNG) {
            return releaseAmount + YOUNG;
        } else if (plantAge == AGE_MATURE) {
            return releaseAmount + MATURE;
        } else if (plantAge == AGE_OLD) {
            return releaseAmount + OLD;
        }
        return 0;
    }
}
