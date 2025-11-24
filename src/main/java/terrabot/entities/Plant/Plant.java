package terrabot.entities.Plant;

import fileio.PlantInput;
import lombok.Getter;
import lombok.Setter;
import terrabot.entities.Entity;

public abstract class Plant extends Entity {
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
}
