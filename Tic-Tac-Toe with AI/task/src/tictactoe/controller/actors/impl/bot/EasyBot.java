package tictactoe.controller.actors.impl.bot;

import tictactoe.model.CellType;
import tictactoe.service.Game;

/**
 * Bot making random turns
 */
public class EasyBot extends AbstractBot {
    protected EasyBot(Game game, CellType cellType) {
        super(game, cellType);
    }

    /**
     * Bot making random move
     */
    @Override
    public void makeMove() {
        var availableTurns = game.getAvailableTurns();
        var botTurn = availableTurns.get(random.nextInt(availableTurns.size()));
        game.makeTurn(botTurn, cellType);
    }
}
