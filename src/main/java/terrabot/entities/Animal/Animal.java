package terrabot.entities.Animal;

import terrabot.entities.Entity;
import lombok.Getter;
import lombok.Setter;
import fileio.AnimalInput;

public abstract class Animal extends Entity {
    @Getter @Setter
    private String type;
    @Getter @Setter
    private String animalStatus;
    @Getter @Setter
    private boolean pendingFertilizer;
    @Getter @Setter
    private String lastAte;
    @Getter @Setter
    private int lastProcessedTime = -1;

    public Animal(final AnimalInput input) {
        super(input.getName(), input.getMass());
        type = input.getType();
        animalStatus = "Hungry";
        pendingFertilizer = false;
        lastAte = "none";
    }

    /**
     * Computes the probability of a specific type of animal to attack the robot.
     *
     * @return the computed attack chance
     */
    public abstract double calculateAttackChance();
}
