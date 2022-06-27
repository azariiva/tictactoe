package tictactoe.controller.impl;

import tictactoe.controller.Controller;
import tictactoe.controller.ControllerBuilder;
import tictactoe.controller.actors.Player;
import tictactoe.controller.actors.PlayerBuilder;
import tictactoe.model.CellType;
import tictactoe.model.GameStatus;
import tictactoe.service.Game;
import tictactoe.view.View;

import java.util.Objects;
import java.util.function.Function;

public class ControllerBuilderImpl implements ControllerBuilder {
    private Game game;
    private PlayerBuilder firstPlayerBuilder;
    private PlayerBuilder secondPlayerBuilder;
    private Function<Game, View> viewSupplier;

    @Override
    public ControllerBuilder setGame(Game game) {
        this.game = game;
        return this;
    }

    @Override
    public ControllerBuilder setFirstPlayerBuilder(PlayerBuilder playerBuilder) {
        this.firstPlayerBuilder = playerBuilder;
        return this;
    }

    @Override
    public ControllerBuilder setSecondPlayerBuilder(PlayerBuilder playerBuilder) {
        this.secondPlayerBuilder = playerBuilder;
        return this;
    }

    @Override
    public ControllerBuilder setViewSupplier(Function<Game, View> viewSupplier) {
        this.viewSupplier = viewSupplier;
        return this;
    }

    @Override
    public Controller build() {
        Objects.requireNonNull(game, GAME_MISSING);
        Objects.requireNonNull(viewSupplier, VIEW_SUPPLIER_MISSING);
        Objects.requireNonNull(firstPlayerBuilder, FP_BUILDER_MISSING);
        Objects.requireNonNull(secondPlayerBuilder, SP_BUILDER_MISSING);
        return new ConsoleController(
                game,
                viewSupplier.apply(game),
                firstPlayerBuilder.setGame(game).setCellType(CellType.FIRST_PLAYER_OCCUPY).build(),
                secondPlayerBuilder.setGame(game).setCellType(CellType.SECOND_PLAYER_OCCUPY).build()
        );
    }

    private static class ConsoleController implements Controller {

        private final Game game;
        private final View view;
        private final Player firstPlayer;
        private final Player secondPlayer;

        ConsoleController(Game game,
                          View view,
                          Player firstPlayer,
                          Player secondPlayer) {
            this.game = game;
            this.view = view;
            this.firstPlayer = firstPlayer;
            this.secondPlayer = secondPlayer;
        }

        @Override
        public void start() {
            view.showBoard();
            while (game.getGameStatus() == GameStatus.NOT_FINISHED) {
                firstPlayer.makeMove();
                view.showBoard();
                if (game.getGameStatus() == GameStatus.NOT_FINISHED) {
                    secondPlayer.makeMove();
                    view.showBoard();
                }
            }
            view.showGameStatus();
        }
    }
}
