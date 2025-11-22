package terrabot.entities.Plant;
import fileio.PlantInput;

public class Ferns extends Plant {
    private static final double STUCK_CHANCE = 0.3;

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
}
