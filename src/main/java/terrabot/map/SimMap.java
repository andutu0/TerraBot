package terrabot.map;

import lombok.Getter;
import lombok.Setter;

public final class SimMap {
    @Getter
    private final int rows;
    @Getter
    private final int columns;
    @Getter
    @Setter
    private Cell[][] map;

    public SimMap(final int rows, final int columns) {
        this.rows = rows;
        this.columns = columns;
        this.map = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.map[i][j] = new Cell();
            }
        }
    }

    /**
     * Retrieves the cell at the specified row and column coordinates.
     *
     * @param row the row index of the cell
     * @param column the column index of the cell
     * @return the Cell at the specified coordinates
     */
    public Cell getCell(final int row, final int column) {
        return this.map[row][column];
    }
}
