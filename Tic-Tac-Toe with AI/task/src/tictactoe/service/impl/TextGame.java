package tictactoe.service.impl;

import tictactoe.exception.UserInputException;
import tictactoe.model.CellCoordinates;
import tictactoe.model.CellType;
import tictactoe.model.GameStatus;
import tictactoe.service.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextGame implements Game {
    static final int BOARD_SIDE_SIZE = 3;
    static final int WIN_CONDITION = 3;

    static final String ILLEGAL_CHARACTER_ERROR_STRING = "Illegal character %c in initial state";

    private final CellType[][] board;
    private final List<CellCoordinates> availableTurns;
    private GameStatus gameStatus;

    public TextGame() {
        board = new CellType[BOARD_SIDE_SIZE][BOARD_SIDE_SIZE];
        availableTurns = new ArrayList<>();
        initializeClear();
    }

    public TextGame(String initialStateString, char emptyChar, char firstPlayerChar, char secondPlayerChar) {
        board = new CellType[BOARD_SIDE_SIZE][BOARD_SIDE_SIZE];
        this.availableTurns = new ArrayList<>();
        for (int i = 0; i < getBoardHeight(); i++) {
            for (int j = 0; j < getBoardWidth(); j++) {
                char initialStateStringCharacter = initialStateString.charAt(i * BOARD_SIDE_SIZE + j);
                CellType initialStateStringCellType;
                if (initialStateStringCharacter == firstPlayerChar) {
                    initialStateStringCellType = CellType.FIRST_PLAYER_OCCUPY;
                } else if (initialStateStringCharacter == secondPlayerChar) {
                    initialStateStringCellType = CellType.SECOND_PLAYER_OCCUPY;
                } else {
                    throw new UserInputException(String.format(ILLEGAL_CHARACTER_ERROR_STRING, initialStateStringCharacter));
                }
                if (initialStateStringCharacter == emptyChar) {
                    addTurn(i + 1, j + 1);
                    board[i][j] = CellType.EMPTY;
                } else {
                    board[i][j] = initialStateStringCellType;
                }
            }
        }
        calculateGameStatus();
    }

    private TextGame(TextGame game) {
        this.board = game.board.clone();
        this.availableTurns = new ArrayList<>(game.availableTurns);
        this.gameStatus = game.gameStatus;
    }

    @Override
    public void makeTurn(int row, int col, CellType cellType) throws RuntimeException {
        if ((getBoardHeight() < row || row < 1) || (getBoardWidth() < col || col < 1)) {
            throw new UserInputException("Coordinates should be from 1 to 3!");
        }
        if (board[row - 1][col - 1] != CellType.EMPTY) {
            throw new UserInputException("This cell is occupied! Choose another one!");
        }
        board[row - 1][col - 1] = cellType;
        availableTurns.removeIf((coordinates -> coordinates.getRow() == row && coordinates.getCol() == col));
        if (gameStatus == GameStatus.NOT_FINISHED) {
            calculateGameStatus();
        }
    }

    @Override
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    @Override
    public CellType[][] getBoard() {
        // defensive copy
        return Game.boardCopy(board, getBoardHeight(), getBoardWidth());
    }

    @Override
    public int getBoardHeight() {
        return BOARD_SIDE_SIZE;
    }

    @Override
    public int getBoardWidth() {
        return BOARD_SIDE_SIZE;
    }

    @Override
    public List<CellCoordinates> getAvailableTurns() {
        return Collections.unmodifiableList(availableTurns);
    }

    @Override
    public void reset() {
        initializeClear();
    }

    @Override
    public int getWinCondition() {
        return WIN_CONDITION;
    }

    private void addTurn(int row, int col) {
        availableTurns.add(new CellCoordinates(row, col));
    }

    private void calculateGameStatus() {
        gameStatus = Game.getGameStatus(getBoard(), getBoardHeight(), getBoardWidth(), getWinCondition());
    }

    /**
     * Defines common logic for default {@link #TextGame() constructor} and  {@link #reset() reset} method
     */
    private void initializeClear() {
        availableTurns.clear();
        gameStatus = GameStatus.NOT_FINISHED;

        for (int i = 0; i < getBoardHeight(); i++) {
            for (int j = 0; j < getBoardWidth(); j++) {
                board[i][j] = CellType.EMPTY;
                addTurn(i + 1, j + 1);
            }
        }
    }
}
