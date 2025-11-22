package terrabot.entities.Animal;

import terrabot.entities.Entity;
import lombok.Getter;
import lombok.Setter;
import fileio.AnimalInput;

public abstract class Animal extends Entity {
    @Getter @Setter
    private String type;

    public Animal(final AnimalInput input) {
        super(input.getName(), input.getMass());
        this.type = input.getType();
    }

    /**
     * Computes the probability of a specific type of animal to attack the robot.
     *
     * @return the computed attack chance
     */
    public abstract double calculateAttackChance();
}
