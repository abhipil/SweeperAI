package io.github.abhipil.sweeper_ai;

import java.util.Random;

/**
 * Created by jay on 12/10/16.
 */
public class Executor {
    private static final String HELP = "Usage - java Executor N freq\n" +
            "\t N - size of the square grid\n" +
            "\t freq - percentage of mines in grid\n";

    public static void main(String[] args) {
        // Parse arguments
        int edgesize=0;
        double mineFreq=0.0d;
        try {
            edgesize = Integer.parseInt(args[0]);
            mineFreq = Double.parseDouble(args[1]);
        } catch (NumberFormatException nfe) {
            printHelp();
            System.exit(0);
        }
        // Initialise Game instance
        Game game = new Game(edgesize, mineFreq);

        // Initialise Player instance
        game.leftClick(3, 5);
        game.print();

        // Initialise game loop
    }

    private static void printHelp() {
        System.out.print(HELP);
    }
}
