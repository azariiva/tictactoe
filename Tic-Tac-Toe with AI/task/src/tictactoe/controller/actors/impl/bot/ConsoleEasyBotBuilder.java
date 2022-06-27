package tictactoe.controller.actors.impl.bot;

import tictactoe.controller.actors.impl.AbstractPlayerBuilder;
import tictactoe.model.CellType;
import tictactoe.service.Game;

import java.util.Objects;

public class ConsoleEasyBotBuilder extends AbstractPlayerBuilder {
    @Override
    public ConsoleEasyBot build() {
        Objects.requireNonNull(game, GAME_MISSING);
        Objects.requireNonNull(cellType, CELL_TYPE_MISSING);
        return new ConsoleEasyBot(game, cellType);
    }

    private static class ConsoleEasyBot extends EasyBot {

        ConsoleEasyBot(Game game, CellType cellType) {
            super(game, cellType);
        }

        @Override
        public void makeMove() {
            System.out.println("Making move level \"easy\"");
            super.makeMove();
        }
    }
}
