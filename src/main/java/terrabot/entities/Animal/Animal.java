package terrabot.entities.Animal;

import terrabot.entities.Entity;
import lombok.Getter;
import lombok.Setter;
import fileio.AnimalInput;

public class Animal extends Entity {
    @Getter @Setter
    private String type;

    public Animal(final AnimalInput input) {
        super(input.getName(), input.getMass());
        this.type = input.getType();
    }
}
