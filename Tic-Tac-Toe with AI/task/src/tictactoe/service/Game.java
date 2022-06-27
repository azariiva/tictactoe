package tictactoe.service;

import tictactoe.model.CellCoordinates;
import tictactoe.model.CellType;
import tictactoe.model.GameStatus;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Game {
    /**
     * Calculate game status on board
     *
     * @param board        Tic-Tac-Toe game board
     * @param height       board height
     * @param width        board width
     * @param winCondition how many characters should be in a row to win a game
     * @return game status
     */
    static GameStatus getGameStatus(CellType[][] board, int height, int width, int winCondition) {
        return Stream.of(
                        traverse(height, (i) -> {
                            var row = board[i];
                            return getGameStatusForLine(width, winCondition, row);
                        }), // check rows for win condition
                        traverse(width, (i) -> {
                            var col = IntStream.range(0, height)
                                    .mapToObj((j) -> board[j][i])
                                    .toArray(CellType[]::new);
                            return getGameStatusForLine(height, winCondition, col);
                        }), // check columns for win condition
                        traverse(height, (i) -> {
                            var diag = IntStream.range(0, Math.min(width, height - i))
                                    .mapToObj((j) -> board[i + j][j])
                                    .toArray(CellType[]::new);
                            return getGameStatusForLine(diag.length, winCondition, diag);
                        }), // check left diagonals for win condition (from top to bottom)
                        traverse(width, (i) -> {
                            var diag = IntStream.range(0, Math.min(width - i, height))
                                    .mapToObj((j) -> board[j][i + j])
                                    .toArray(CellType[]::new);
                            return getGameStatusForLine(diag.length, winCondition, diag);
                        }), // check left diagonals for win condition (from left to right)
                        traverse(height, (i) -> {
                            var diag = IntStream.range(0, Math.min(width, height - i))
                                    .mapToObj((j) -> board[i + j][width - (j + 1)])
                                    .toArray(CellType[]::new);
                            return getGameStatusForLine(diag.length, winCondition, diag);
                        }), // check right diagonals for win condition (from top to bottom)
                        traverse(height, (i) -> {
                            var diag = IntStream.range(0, Math.min(width - i, height))
                                    .mapToObj((j) -> board[j][width - (i + j + 1)])
                                    .toArray(CellType[]::new);
                            return getGameStatusForLine(diag.length, winCondition, diag);
                        }), // check right diagonals for win condition (from left to right)
                        traverse(height, (i) -> {
                            var row = board[i];
                            return traverse(width, (j) -> {
                                if (row[j] == CellType.EMPTY) {
                                    return GameStatus.NOT_FINISHED;
                                }
                                return null;
                            });
                        })// if no one wins than game might be not finished
                )
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(GameStatus.DRAW /* if game finished and no one wins than it is draw */);
    }

    /**
     * Get game status for vertical, horizontal or diagonal line of board
     *
     * @param length       length of line
     * @param winCondition win condition of the game
     * @param line         line of board
     * @return game status or null if win condition hasn't been met
     */
    private static GameStatus getGameStatusForLine(int length, int winCondition, CellType[] line) {
        for (int j = 0; j < length && length - j >= winCondition; j++) {
            if (line[j] != CellType.EMPTY) {
                var probableWinnerCellType = line[j];
                for (int k = j + 1, ctr = 1; k < length && ctr < winCondition; k++, ctr++) {
                    if (probableWinnerCellType != line[k]) {
                        probableWinnerCellType = CellType.EMPTY;
                        break;
                    }
                }
                if (probableWinnerCellType != CellType.EMPTY) {
                    return gameStatusFromCellType(probableWinnerCellType);
                }
            }
        }
        return null;
    }

    /**
     * Traverse arr and apply given {@code traverseFunction}
     * @param size             size of {@code arr}
     * @param traverseFunction use index as parameter and if this function return non-null value of type {@code T} traverse terminates
     * @return result of the last {@code traverseFunction} call
     */
    private static <T> T traverse(int size, Function<Integer, T> traverseFunction) {
        T result = null;
        for (int i = 0; i < size && result == null; i++) {
            result = traverseFunction.apply(i);
        }
        return result;
    }

    /**
     * Translate {@link CellType} to {@link GameStatus} following rules
     * <ul>
     *     <li>If cell type is {@code CellType.FIRST_PLAYER_OCCUPY} then return {@code GameStatus.FIRST_PLAYER_WIN}</li>
     *     <li>If cell type is {@code CellType.SECOND_PLAYER_OCCUPY} then return {@code GameStatus.SECOND_PLAYER_WIN}</li>
     *     <li>Other cases return {@code null}</li>
     * </ul>
     *
     * @param cellType type of cell
     * @return status of the game based on {@code cellType}
     */
    static GameStatus gameStatusFromCellType(CellType cellType) {
        switch (cellType) {
            case FIRST_PLAYER_OCCUPY:
                return GameStatus.FIRST_PLAYER_WIN;
            case SECOND_PLAYER_OCCUPY:
                return GameStatus.SECOND_PLAYER_WIN;
        }
        return null;
    }

    /**
     * Make a deep copy of the {@code board} with dimensions {@code height} and {@code width}
     * @param board board
     * @param height height of the {@code board}
     * @param width width of the {@code board}
     * @return deep copy of the {@code board}
     */
    static CellType[][] boardCopy(CellType[][] board, int height, int width) {
        var boardCopy = new CellType[height][width];
        for (int i = 0; i < height; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, width);
        }
        return boardCopy;
    }

    void makeTurn(int row, int col, CellType cellType) throws RuntimeException;

    default void makeTurn(CellCoordinates coordinates, CellType cellType) {
        makeTurn(coordinates.getRow(), coordinates.getCol(), cellType);
    }

    default GameStatus getGameStatus() {
        return getGameStatus(getBoard(), getBoardHeight(), getBoardWidth(), getWinCondition());
    }

    CellType[][] getBoard();

    int getBoardHeight();

    int getBoardWidth();

    List<CellCoordinates> getAvailableTurns();

    void reset();

    int getWinCondition();
}
