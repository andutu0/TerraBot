package terrabot.entities.Plant;
import fileio.PlantInput;

public class Algae extends Plant {
    private static final double STUCK_CHANCE = 0.2;

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
}
