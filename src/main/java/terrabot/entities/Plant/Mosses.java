package terrabot.entities.Plant;
import fileio.PlantInput;

public class Mosses extends Plant {
    private static final double STUCK_CHANCE = 0.4;

    public Mosses(final PlantInput input) {
        super(input);
    }

    /**
     * Computes the probability of Mosses to get the robot stuck.
     *
     * @return the computed stuck chance
     */
    public double getStuckChance() {
        return STUCK_CHANCE;
    }
}
