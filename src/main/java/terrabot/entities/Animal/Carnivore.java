package terrabot.entities.Animal;
import fileio.AnimalInput;

public final class Carnivore extends Animal {
    private static final double ATTACK_POSSIBILITY = 30;
    private static final double MAX = 100.0;
    private static final double DIVIDER = 10.0;

    public Carnivore(final AnimalInput input) {
        super(input);
    }

    /**
     * Computes the probability of a carnivore attacking the robot.
     *
     * @return the computed attack chance
     */
    public double calculateAttackChance() {
        return ((MAX - ATTACK_POSSIBILITY) / DIVIDER);
    }
}
