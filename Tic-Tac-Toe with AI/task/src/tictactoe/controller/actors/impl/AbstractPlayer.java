package tictactoe.controller.actors.impl;

import tictactoe.controller.actors.Player;
import tictactoe.model.CellType;
import tictactoe.service.Game;

/**
 * Abstract base class for players <br>
 * I assume that each player has {@link AbstractPlayer#game} and {@link AbstractPlayer#cellType cell type}
 */
public abstract class AbstractPlayer implements Player {
    protected final Game game;
    protected final CellType cellType;

    public AbstractPlayer(Game game, CellType cellType) {
        this.game = game;
        this.cellType = cellType;
    }
}
