package terrabot.entities.Plant;
import fileio.PlantInput;

public class GymnospermsPlants extends Plant {
    private static final double STUCK_CHANCE = 0.6;

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
}
