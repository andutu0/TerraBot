package terrabot.map;

import lombok.Getter;
import lombok.Setter;
import terrabot.entities.Soil.Soil;
import terrabot.entities.Water.Water;
import terrabot.entities.Air.Air;
import terrabot.entities.Plant.Plant;
import terrabot.entities.Animal.Animal;

public class Cell {
    @Getter @Setter
    private Soil soil;
    @Getter @Setter
    private Air air;
    @Getter @Setter
    private Water water;
    @Getter @Setter
    private Plant plant;
    @Getter @Setter
    private Animal animal;
}
