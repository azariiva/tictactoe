package tictactoe.controller.actors.impl.bot;

import tictactoe.exception.InternalLogicException;
import tictactoe.model.CellCoordinates;
import tictactoe.model.CellType;
import tictactoe.service.Game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Bot calculating all available turns and selecting best one using minimax algorithm <br>
 *
 * Note: needs optimization for big boards
 * @see <a href="https://www.freecodecamp.org/news/how-to-make-your-tic-tac-toe-game-unbeatable-by-using-the-minimax-algorithm-9d690bad4b37/">freeCodeCamp</a>
 */
public class HardBot extends AbstractBot {

    public HardBot(Game game, CellType cellType) {
        super(game, cellType);
    }

    /**
     * Turn which has score according to minimax algorithm
     */
    private static final class ScoredTurn {
        private int score;
        private CellCoordinates turn;

        private ScoredTurn() {
            this.score = 0;
            this.turn = null;
        }

        private ScoredTurn(int score, CellCoordinates turn) {
            this.score = score;
            this.turn = turn;
        }

        private ScoredTurn(int score) {
            this.score = score;
            this.turn = null;
        }

        void setScore(int score) {
            this.score = score;
        }

        public void setTurn(CellCoordinates turn) {
            this.turn = turn;
        }


        public int score() {
            return score;
        }

        public CellCoordinates index() {
            return turn;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (ScoredTurn) obj;
            return this.score == that.score &&
                    Objects.equals(this.turn, that.turn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(score, turn);
        }

        @Override
        public String toString() {
            return "ScoredTurn[" +
                    "score=" + score + ", " +
                    "index=" + turn + ']';
        }
    }

    @Override
    public void makeMove() {
        var winTurn = minimax(game.getBoard(), game.getAvailableTurns(), cellType, 1).turn;
        if (winTurn == null) {
            throw new InternalLogicException("Received empty turn");
        }
        game.makeTurn(winTurn, cellType);
    }

    private ScoredTurn minimax(CellType[][] board, List<CellCoordinates> availableTurns, CellType currentPlayerCellType, int depth) {
        var gameStatus = Game.getGameStatus(board, game.getBoardHeight(), game.getBoardWidth(), game.getWinCondition());

        switch (gameStatus) {
            case NOT_FINISHED:
                break;
            case DRAW:
                return new ScoredTurn(0);
            default:
                return gameStatus == Game.gameStatusFromCellType(cellType) ? new ScoredTurn(10 - depth) : new ScoredTurn(depth - 10);
        }

        var availableTurnsCopy = new ArrayList<>(availableTurns);
        var movesStream = availableTurns.stream()
                .map(turn -> {
                    board[turn.getRow() - 1][turn.getCol() - 1] = currentPlayerCellType; // make turn
                    availableTurnsCopy.remove(turn); // remove turn from available
                    var scoredTurn = minimax(board, availableTurnsCopy, CellType.oppositeOf(currentPlayerCellType), depth + 1);
                    board[turn.getRow() - 1][turn.getCol() - 1] = CellType.EMPTY; // rollback board
                    availableTurnsCopy.add(turn); // rollback available turns
                    scoredTurn.setTurn(turn);
                    return scoredTurn;
                });
        if (currentPlayerCellType == cellType) {
            return movesStream.max(Comparator.comparingInt(m -> m.score)).orElseGet(ScoredTurn::new);
        }
        return movesStream.min(Comparator.comparingInt(m -> m.score)).orElseGet(ScoredTurn::new);
    }
}
