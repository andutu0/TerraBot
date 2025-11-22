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
    private int energyPoints;

    public TerraBot(final Position startPos, final int energyPoints) {
        this.position = startPos;
        this.energyPoints = energyPoints;
    }

    /**
     * Moves the bot one unit to the right.
     */
    public void moveRight() {
        this.position.setX(this.position.getX() + 1);
    }

    /**
     * Moves the bot one unit to the left.
     */
    public void moveLeft() {
        this.position.setX(this.position.getX() - 1);
    }

    /**
     * Moves the bot one unit up.
     */
    public void moveUp() {
        this.position.setY(this.position.getY() + 1);
    }

    /**
     * Moves the bot one unit down.
     */
    public void moveDown() {
        this.position.setY(this.position.getY() - 1);
    }
}
