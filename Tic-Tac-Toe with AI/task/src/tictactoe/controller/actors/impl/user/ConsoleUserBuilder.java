package tictactoe.controller.actors.impl.user;

import tictactoe.controller.actors.impl.AbstractPlayer;
import tictactoe.controller.actors.impl.AbstractPlayerBuilder;
import tictactoe.exception.UserInputException;
import tictactoe.model.CellType;
import tictactoe.service.Game;

import java.util.Objects;
import java.util.Scanner;

public class ConsoleUserBuilder extends AbstractPlayerBuilder {
    @Override
    public ConsoleUserPlayer build() {
        Objects.requireNonNull(game, GAME_MISSING);
        Objects.requireNonNull(cellType, CELL_TYPE_MISSING);
        return new ConsoleUserPlayer(game, cellType);
    }

    private static class ConsoleUserPlayer extends AbstractPlayer {
        private final Scanner scanner = new Scanner(System.in);

        public ConsoleUserPlayer(Game game, CellType cellType) {
            super(game, cellType);
        }

        @Override
        public void makeMove() {
            System.out.print("Enter the coordinates: ");
            while (true) {
                scanner.useDelimiter("\\R");
                if (!scanner.hasNext("\\d+ \\d+")) {
                    scanner.nextLine();
                    System.out.println("You should enter numbers!");
                } else {
                    scanner.useDelimiter("\\s+");
                    try {
                        game.makeTurn(scanner.nextInt(), scanner.nextInt(), cellType);
                        break;
                    } catch (UserInputException exc) {
                        System.out.println(exc.getMessage());
                    }
                }
            }
        }
    }
}
