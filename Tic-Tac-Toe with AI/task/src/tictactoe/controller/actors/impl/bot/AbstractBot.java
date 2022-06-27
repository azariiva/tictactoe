package tictactoe.controller.actors.impl.bot;

import tictactoe.controller.actors.impl.AbstractPlayer;
import tictactoe.model.CellType;
import tictactoe.service.Game;

import java.util.Random;

public abstract class AbstractBot extends AbstractPlayer {
    protected static final Random random = new Random();

    public AbstractBot(Game game, CellType cellType) {
        super(game, cellType);
    }
}
