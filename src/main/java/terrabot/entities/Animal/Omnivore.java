package terrabot.entities.Animal;
import fileio.AnimalInput;

public final class Omnivore extends Animal {
    private static final double ATTACK_POSSIBILITY = 60;
    private static final double MAX = 100.0;
    private static final double DIVIDER = 10.0;

    public Omnivore(final AnimalInput input) {
        super(input);
    }

    /**
     * Computes the probability of an omnivore attacking the robot.
     *
     * @return the computed attack chance
     */
    public double calculateAttackChance() {
        return ((MAX - ATTACK_POSSIBILITY) / DIVIDER);
    }
}
