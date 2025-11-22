package terrabot;

import lombok.Getter;
import lombok.Setter;
import terrabot.entities.Position;

public final class TerraBot {

    @Getter
    @Setter
    private Position position;

    @Getter
    @Setter
    private int energyStatus;

    public TerraBot(final Position startPos, final int energyStatus) {
        this.position = startPos;
        this.energyStatus = energyStatus;
    }
}
