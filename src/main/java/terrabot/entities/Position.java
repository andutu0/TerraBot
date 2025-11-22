package terrabot.entities;

public final class Position {
    private static final int HASH_MULTIPLIER = 31;
    private int x;
    private int y;

    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setX(final int x) {
        this.x = x;
    }
    public void setY(final int y) {
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
