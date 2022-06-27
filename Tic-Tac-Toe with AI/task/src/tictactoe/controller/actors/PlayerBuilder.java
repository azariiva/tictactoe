package tictactoe.controller.actors;

import tictactoe.model.CellType;
import tictactoe.service.Game;

/**
 * Base interface for player builder
 */
public interface PlayerBuilder {
    String GAME_MISSING = "You must specify game before building player";
    String CELL_TYPE_MISSING = "You must specify cell type before building player";

    PlayerBuilder setGame(Game game);

    PlayerBuilder setCellType(CellType cellType);

    Player build();
}
