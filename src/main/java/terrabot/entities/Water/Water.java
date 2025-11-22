package terrabot.entities.Water;

import fileio.WaterInput;
import lombok.Getter;
import lombok.Setter;
import terrabot.entities.Entity;

// fara subclase pentru ca nu folosim tipurile la nimic

public final class Water extends Entity {
    @Getter @Setter
    private double salinity;
    @Getter @Setter
    private double pH;
    @Getter @Setter
    private double purity;
    @Getter @Setter
    private double turbidity;
    @Getter @Setter
    private double contaminantIndex;
    @Getter @Setter
    private boolean isFrozen;
    @Getter @Setter
    private String type;

    public Water(final WaterInput input) {
        super(input.getName(), input.getMass());
        this.salinity = input.getSalinity();
        this.purity = input.getPurity();
        this.turbidity = input.getTurbidity();
        this.contaminantIndex = input.getContaminantIndex();
        this.isFrozen = input.isFrozen();
        this.type = input.getType();
    }
}
