package terrabot.entities;

import lombok.Getter;
import lombok.Setter;

public abstract class Entity {
    @Getter
    private final String name;
    @Getter
    private final double mass;
    @Setter @Getter
    private Position position;
    @Getter @Setter
    private boolean scanned;
    @Getter @Setter
    private int scannedTimestamp;

    public Entity(final String name, final double mass) {
        this.name = name;
        this.mass = mass;
        scanned = false;
    }
}
