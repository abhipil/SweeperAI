package io.github.abhipil.sweeper_ai;

import io.github.abhipil.sweeper_ai.agent.Agent;
import io.github.abhipil.sweeper_ai.agent.HumanAgent;

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
        int edgeSize=0;
        double mineFreq=0.0d;
        try {
            edgeSize = Integer.parseInt(args[0]);
            mineFreq = Double.parseDouble(args[1]);
        } catch (NumberFormatException nfe) {
            printHelp();
            System.exit(0);
        }

        /*
        *  Game game = new Game(edgesize, mineFreq);
        *
        *  game.leftClick(3, 5);
        *  game.print();
        * */
        // Initialise Game instance

        Executor executor = new Executor();
        Agent agent = new HumanAgent();
        executor.runSingleGame(agent, edgeSize, mineFreq);

    }

    private static void runSingleGame(Agent agent, int edgeSize, double mineFreq) {


        Game game = new Game(edgeSize,mineFreq);
        int moves =0;
        while (!game.isOver()) {

            if (agent instanceof HumanAgent) {
                game.print();
            }
            game.leftClick(agent.getMove(game));
            moves ++;
        }
        game.print();
        if(game.isWon()) {
            System.out.println("You have won the game in "+moves+" moves");
        } else {
            System.out.println("You lost!!");
        }
    }

    private static void printHelp() {
        System.out.print(HELP);
    }
}
