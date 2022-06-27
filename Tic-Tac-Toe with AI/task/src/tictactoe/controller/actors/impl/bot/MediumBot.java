package tictactoe.controller.actors.impl.bot;

import tictactoe.model.CellCoordinates;
import tictactoe.model.CellType;
import tictactoe.service.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Bot preventing your immediate win and making win turn if it can
 * Note: works as expected only on 3x3 board with {@code winCondition = 3}
 */
public class MediumBot extends AbstractBot {

    private final CellType opponentCellType;

    protected MediumBot(Game game, CellType cellType) {
        super(game, cellType);
        opponentCellType = CellType.oppositeOf(cellType);
    }

    /**
     * Bot making move following rules below choosing random turn from corresponding subsets
     * <ol>
     *     <li> If it already has two in a row and can win with one further move, it does so. </li>
     *     <li> If its opponent can win with one move, it plays the move necessary to block this. </li>
     *     <li> Otherwise, it makes a random move. </li>
     * </ol>
     */
    @Override
    public void makeMove() {
        var winTurns = getWinTurns(cellType);
        var opponentWinTurns = getWinTurns(opponentCellType);
        var availableTurns = game.getAvailableTurns();

        //noinspection OptionalGetWithoutIsPresent
        var botTurns = Stream
                .of(winTurns, opponentWinTurns, availableTurns)
                .filter((turns) -> !turns.isEmpty())
                .findFirst()
                .get();
        var botTurn = botTurns.get(random.nextInt(botTurns.size()));
        game.makeTurn(botTurn, cellType);
    }

    /**
     * Check field for all possible win turns
     * @param cellType player cell type
     * @return all possible win turns
     */
    private List<CellCoordinates> getWinTurns(CellType cellType) {
        final CellType[][] board = game.getBoard();

        return Stream.of(
                getRowWinTurns(board, cellType),
                getColWinTurns(board, cellType),
                getLeftDiagonalWinTurn(board, cellType),
                getRightDiagonalWinTurn(board, cellType)
        ).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * Check horizontal lines for possible win turns
     * @param board tic-tac-toe game board
     * @param cellType player cell type
     * @return win turns possible in rows
     */
    private List<CellCoordinates> getRowWinTurns(CellType[][] board, CellType cellType) {
        final List<CellCoordinates> winTurns = new ArrayList<>();

        // check vertical lines
        IntStream.range(0, game.getBoardHeight())
                .forEach((i) -> {
                    final var row = board[i];
                    final var emptyCellsInRow = IntStream
                            .range(0, game.getBoardWidth())
                            .filter((j) -> row[j] == CellType.EMPTY)
                            .toArray();

                    // if bot has two in a row and can make win turn
                    if (emptyCellsInRow.length == 1 &&
                            Arrays.stream(row).filter((boardCellType) -> boardCellType == cellType).count() == 2) {
                        winTurns.add(new CellCoordinates(i + 1, emptyCellsInRow[0] + 1));
                    }
                });
        return winTurns;
    }

    /**
     * Check vertical lines for possible win turns
     * @param board tic-tac-toe game board
     * @param cellType player cell type
     * @return win turns possible in columns
     */
    private List<CellCoordinates> getColWinTurns(CellType[][] board, CellType cellType) {
        final List<CellCoordinates> winTurns = new ArrayList<>();

        IntStream.range(0, game.getBoardWidth())
                .forEach((i) -> {
                    final var col = IntStream.
                            range(0, game.getBoardHeight())
                            .mapToObj((j) -> board[j][i])
                            .toArray();
                    final var emptyCellsInCol = IntStream
                            .range(0, col.length)
                            .filter((j) -> col[j] == CellType.EMPTY)
                            .toArray();

                    // if bot has two in a column and can make win turn
                    if (emptyCellsInCol.length == 1 &&
                            Arrays.stream(col).filter((boardCellType) -> boardCellType == cellType).count() == 2) {
                        winTurns.add(new CellCoordinates(emptyCellsInCol[0] + 1, i + 1));
                    }
                });
        return winTurns;
    }

    /**
     * Check left diagonal for possible win turn
     * @param board tic-tac-toe game board
     * @param cellType player cell type
     * @return win turn possible in left diagonal
     */
    private List<CellCoordinates> getLeftDiagonalWinTurn(CellType[][] board, CellType cellType) {
        final List<CellCoordinates> winTurns = new ArrayList<>();

        final var leftDiagonal = IntStream
                .range(0, Math.min(game.getBoardHeight(), game.getBoardWidth()))
                .mapToObj((i) -> board[i][i])
                .toArray();
        final var emptyCellsInLeftDiagonal = IntStream
                .range(0, leftDiagonal.length)
                .filter((i) -> board[i][i] == CellType.EMPTY)
                .toArray();

        // if bot has two in a left diagonal and can make win turn
        if (emptyCellsInLeftDiagonal.length == 1 &&
                Arrays.stream(leftDiagonal).filter((boardCellType) -> boardCellType == cellType).count() == 2) {
            winTurns.add(new CellCoordinates(emptyCellsInLeftDiagonal[0] + 1, emptyCellsInLeftDiagonal[0] + 1));
        }
        return winTurns;
    }

    /**
     * Check right diagonal for possible win turn
     * @param board tic-tac-toe game board
     * @param cellType player cell type
     * @return win turn possible in right diagonal
     */
    private List<CellCoordinates> getRightDiagonalWinTurn(CellType[][] board, CellType cellType) {
        final List<CellCoordinates> winTurns = new ArrayList<>();

        // check right diagonal
        final var rightDiagonal = IntStream
                .range(0, Math.min(game.getBoardHeight(), game.getBoardWidth()))
                .mapToObj((i) -> board[i][game.getBoardWidth() - 1 - i])
                .toArray();
        final var emptyCellsInRightDiagonal = IntStream
                .range(0, rightDiagonal.length)
                .filter((i) -> board[i][game.getBoardWidth() - 1 - i] == CellType.EMPTY)
                .toArray();

        // if bot has two in a right diagonal and can make win turn
        if (emptyCellsInRightDiagonal.length == 1 &&
                Arrays.stream(rightDiagonal).filter((boardCellType) -> boardCellType == cellType).count() == 2) {
            winTurns.add(new CellCoordinates(emptyCellsInRightDiagonal[0] + 1, game.getBoardWidth() - 1 - emptyCellsInRightDiagonal[0] + 1));
        }
        return winTurns;
    }
}
