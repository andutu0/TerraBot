package terrabot.entities.Animal;
import fileio.AnimalInput;

public final class Herbivore extends Animal {
    private static final double ATTACK_POSSIBILITY = 85;
    private static final double MAX = 100.0;
    private static final double DIVIDER = 10.0;

    public Herbivore(final AnimalInput input) {
        super(input);
    }

    /**
     * Computes the probability of a herbivore attacking the robot.
     *
     * @return the computed attack chance
     */
    public double calculateAttackChance() {
        return ((MAX - ATTACK_POSSIBILITY) / DIVIDER);
    }
}
