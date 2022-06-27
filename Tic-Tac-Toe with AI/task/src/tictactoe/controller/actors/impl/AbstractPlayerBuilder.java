package tictactoe.controller.actors.impl;

import tictactoe.controller.actors.PlayerBuilder;
import tictactoe.model.CellType;
import tictactoe.service.Game;

/**
 * Abstract base class for players
 * @see tictactoe.controller.actors.impl.AbstractPlayer
 */
public abstract class AbstractPlayerBuilder implements PlayerBuilder {
    protected Game game;
    protected CellType cellType;

    @Override
    public PlayerBuilder setGame(Game game) {
        this.game = game;
        return this;
    }

    @Override
    public PlayerBuilder setCellType(CellType cellType) {
        this.cellType = cellType;
        return this;
    }
}
