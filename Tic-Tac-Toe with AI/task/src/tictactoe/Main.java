package tictactoe;

import tictactoe.controller.actors.PlayerBuilder;
import tictactoe.controller.actors.impl.bot.ConsoleHardBotBuilder;
import tictactoe.controller.actors.impl.bot.ConsoleEasyBotBuilder;
import tictactoe.controller.actors.impl.bot.ConsoleMediumBotBuilder;
import tictactoe.controller.actors.impl.user.ConsoleUserBuilder;
import tictactoe.controller.impl.ControllerBuilderImpl;
import tictactoe.service.impl.TextGame;
import tictactoe.view.impl.ConsoleView;

import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

public class Main {
    static final Map<String, Supplier<PlayerBuilder>> TAC_TOE_PLAYER_MAP = Map.of(
            "easy", ConsoleEasyBotBuilder::new,
            "user", ConsoleUserBuilder::new,
            "medium", ConsoleMediumBotBuilder::new,
            "hard", ConsoleHardBotBuilder::new
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        final var game = new TextGame();
        while (true) {
            System.out.print("Input command: ");
            String inputLine = scanner.nextLine();
            String[] inArgs = inputLine.split("\\s");
            if (inArgs.length == 0) {
                System.out.println("Bad parameters!");
                continue;
            }
            switch (inArgs[0]) {
                case "exit":
                    return;
                case "start":
                    if (inArgs.length < 3) {
                        System.out.println("Bad parameters!");
                        continue;
                    }
                    Supplier<PlayerBuilder> firstPlayerBuilder = TAC_TOE_PLAYER_MAP.get(inArgs[1]);
                    Supplier<PlayerBuilder> secondPlayerBuilder = TAC_TOE_PLAYER_MAP.get(inArgs[2]);
                    if (firstPlayerBuilder == null || secondPlayerBuilder == null) {
                        System.out.println("Bad parameters!");
                        continue;
                    }
                    game.reset();
                    new ControllerBuilderImpl()
                            .setGame(game)
                            .setFirstPlayerBuilder(firstPlayerBuilder.get())
                            .setSecondPlayerBuilder(secondPlayerBuilder.get())
                            .setViewSupplier((g) -> new ConsoleView(g, ' ', 'X', 'O'))
                            .build()
                            .start();
                    break;
                default:
                    System.out.println("Bad parameters!");
                    break;
            }
        }
    }
}
