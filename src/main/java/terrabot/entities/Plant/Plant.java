package terrabot.entities.Plant;

import fileio.PlantInput;
import lombok.Getter;
import lombok.Setter;
import terrabot.entities.Entity;

public class Plant extends Entity {
    @Getter @Setter
    private String type;

    public Plant(final PlantInput input) {
        super(input.getName(), input.getMass());
        this.type = input.getType();
    }
}
