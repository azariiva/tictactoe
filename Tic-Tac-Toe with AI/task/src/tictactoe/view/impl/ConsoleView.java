package tictactoe.view.impl;

import tictactoe.service.Game;
import tictactoe.exception.InternalModelException;
import tictactoe.model.CellType;
import tictactoe.model.GameStatus;
import tictactoe.view.View;

import java.util.Map;

public class ConsoleView implements View {
    private static final String WINNER_FORMAT_STRING = "%c wins";
    private static final String UNKNOWN_GAME_STATUS_ERROR_STRING = "Unknown game status %s";

    private final Game game;
    private final char emptyChar;
    private final char firstPlayerChar;
    private final char secondPlayerChar;
    private final Map<GameStatus, String> gameStatusStringMap;

    public ConsoleView(Game game, char emptyChar, char firstPlayerChar, char secondPlayerChar) {
        this.game = game;
        this.emptyChar = emptyChar;
        this.firstPlayerChar = firstPlayerChar;
        this.secondPlayerChar = secondPlayerChar;
        this.gameStatusStringMap = Map.of(
                GameStatus.DRAW, "Draw",
                GameStatus.NOT_FINISHED, "Game not finished",
                GameStatus.FIRST_PLAYER_WIN, String.format(WINNER_FORMAT_STRING, firstPlayerChar),
                GameStatus.SECOND_PLAYER_WIN, String.format(WINNER_FORMAT_STRING, secondPlayerChar)
        );
    }

    @Override
    public void showBoard() {
        System.out.println(getBoardString(game.getBoard(), game.getBoardHeight(), game.getBoardWidth()));
    }

    @Override
    public void showGameStatus() {
        GameStatus status = game.getGameStatus();
        String gameStatusString = gameStatusStringMap.get(status);

        if (gameStatusString == null) {
            throw new InternalModelException(String.format(UNKNOWN_GAME_STATUS_ERROR_STRING, status));
        } else {
            System.out.println(gameStatusStringMap.get(game.getGameStatus()));
        }
    }

    public String getBoardString(CellType[][] board, int height, int width) {
        StringBuilder sb = new StringBuilder();

        sb.append("---".repeat(width)).append("\n");
        for (int i = 0; i < height; i++) {
            sb.append("| ");
            for (int j = 0; j < width; j++) {
                sb.append(cellTypeToChar(board[i][j])).append(' ');
            }
            sb.append("|\n");
        }
        sb.append("---".repeat(width));
        return sb.toString();
    }

    private char cellTypeToChar(CellType cellType) {
        switch (cellType) {
            case EMPTY:
                return emptyChar;
            case FIRST_PLAYER_OCCUPY:
                return firstPlayerChar;
            case SECOND_PLAYER_OCCUPY:
                return secondPlayerChar;
            default:
                return 0; // unreachable
        }
    }
}
