package tictactoe.controller.actors.impl.bot;

import tictactoe.controller.actors.Player;
import tictactoe.controller.actors.impl.AbstractPlayerBuilder;
import tictactoe.controller.actors.impl.bot.HardBot;
import tictactoe.model.CellType;
import tictactoe.service.Game;

import java.util.Objects;

public class ConsoleHardBotBuilder extends AbstractPlayerBuilder {
    @Override
    public Player build() {
        Objects.requireNonNull(game, GAME_MISSING);
        Objects.requireNonNull(cellType, CELL_TYPE_MISSING);
        return new ConsoleHardBot(game, cellType);
    }

    private static class ConsoleHardBot extends HardBot {

        public ConsoleHardBot(Game game, CellType cellType) {
            super(game, cellType);
        }

        @Override
        public void makeMove() {
            System.out.println("Making move level \"hard\"");
            super.makeMove();
        }
    }
}
