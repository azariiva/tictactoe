package tictactoe.model;

import java.util.Objects;

public class CellCoordinates {
    private final int row;
    private final int col;

    public CellCoordinates(int row, int col) {
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellCoordinates that = (CellCoordinates) o;
        return col == that.col && row == that.row;
    }

    @Override
    public String toString() {
        return "CellCoordinates{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
