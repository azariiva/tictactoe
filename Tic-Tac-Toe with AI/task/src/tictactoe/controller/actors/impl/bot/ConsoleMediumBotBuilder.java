package tictactoe.controller.actors.impl.bot;

import tictactoe.controller.actors.Player;
import tictactoe.controller.actors.impl.AbstractPlayerBuilder;
import tictactoe.model.CellType;
import tictactoe.service.Game;

import java.util.Objects;

public class ConsoleMediumBotBuilder extends AbstractPlayerBuilder {

    @Override
    public Player build() {
        Objects.requireNonNull(game, GAME_MISSING);
        Objects.requireNonNull(cellType, CELL_TYPE_MISSING);
        return new ConsoleMediumBot(game, cellType);
    }

    private static class ConsoleMediumBot extends MediumBot {

        public ConsoleMediumBot(Game game, CellType cellType) {
            super(game, cellType);
        }

        @Override
        public void makeMove() {
            System.out.println("Making move level \"medium\"");
            super.makeMove();
        }
    }
}
