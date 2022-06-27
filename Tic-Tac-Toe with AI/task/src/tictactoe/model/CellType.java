package tictactoe.model;

import tictactoe.exception.InternalLogicException;

public enum CellType {
    EMPTY,
    FIRST_PLAYER_OCCUPY,
    SECOND_PLAYER_OCCUPY;

    static final String OPPOSITE_OF_CELL_TYPE_MISSING = "Opposite of %s is missing";

    public static CellType oppositeOf(CellType cellType) {
        switch (cellType) {
            case FIRST_PLAYER_OCCUPY:
                return CellType.SECOND_PLAYER_OCCUPY;
            case SECOND_PLAYER_OCCUPY:
                return CellType.FIRST_PLAYER_OCCUPY;
            default:
                throw new InternalLogicException(String.format(OPPOSITE_OF_CELL_TYPE_MISSING, cellType));
        }
    }
}
