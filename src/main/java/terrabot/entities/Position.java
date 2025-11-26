package terrabot.entities;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class Position {
    private static final int HASH_MULTIPLIER = 31;
    private int x;
    private int y;

    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = HASH_MULTIPLIER * result + y;
        return result;
    }
}
