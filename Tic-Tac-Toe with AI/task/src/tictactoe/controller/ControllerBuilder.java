package tictactoe.controller;

import tictactoe.controller.actors.PlayerBuilder;
import tictactoe.service.Game;
import tictactoe.view.View;

import java.util.function.Function;

public interface ControllerBuilder {
    String GAME_MISSING = "You must specify game before building controller";
    String FP_BUILDER_MISSING = "You must specify first player builder before building controller";
    String SP_BUILDER_MISSING = "You must specify second player builder before building controller";
    String VIEW_SUPPLIER_MISSING = "You must specify view supplier before building controller";

    ControllerBuilder setGame(Game game);
    ControllerBuilder setFirstPlayerBuilder(PlayerBuilder playerBuilder);
    ControllerBuilder setSecondPlayerBuilder(PlayerBuilder playerBuilder);
    ControllerBuilder setViewSupplier(Function<Game, View> viewSupplier);
    Controller build();
}
