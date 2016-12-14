package io.github.abhipil.sweeper_ai;

import io.github.abhipil.sweeper_ai.agent.AStarAgent;
import io.github.abhipil.sweeper_ai.agent.Agent;
import io.github.abhipil.sweeper_ai.agent.HumanAgent;
import io.github.abhipil.sweeper_ai.game.Position;
import io.github.abhipil.sweeper_ai.game.Square;

import java.util.HashMap;
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

        int numTrials = 100;
        Executor executor = new Executor();
        Agent agent = new AStarAgent();
        executor.runSingleGame(agent, edgeSize, mineFreq, true);
//        executor.runExperiment(agent,edgeSize,mineFreq,numTrials);
    }

    private void runExperiment(Agent agent, int edgeSize, double mineFreq, int numTrials) {
        int numWon = 0;
        int[] winMoves = new int[numTrials];
        for (int i=0; i<numTrials; i++) {
            System.out.println("Game "+(i+1)+" intialised");
            Game game = new Game(edgeSize, mineFreq);
            int moves =0;
            Position move;
            while (!game.isOver()) {
                move = agent.getMove(game);
                game.leftClick(move);
                moves ++;
            }
            game.print();
            if(game.isWon()) {
                System.out.println("You have won the game in "+moves+" moves");
                numWon++;
            }
            winMoves[i] = moves;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("The Astar agent won ")
                .append(numWon)
                .append(" times in ")
                .append(numTrials)
                .append(" trials!").append('\n');
        for(int i=0; i< numTrials; i++) {
            sb.append(i).append("  ");
        }
        sb.append('\n');
        for(int i=0; i< numTrials; i++) {
            sb.append(winMoves[i]).append("  ");
        }
        sb.append('\n');
        System.out.print(sb);
    }


    private void runSingleGame(Agent agent, Game game, boolean visual) {
        int moves =0;
        Position move = new Position(0,0);
        while (!game.isOver()) {
            if (visual) {
                game.print();
            }
            move = agent.getMove(game);
            game.leftClick(move);
            moves ++;
        }
        game.print();
        if(game.isWon()) {
            System.out.println("You have won the game in "+moves+" moves");
        } else {
            System.out.println("You lost!! Landed on mine at "+move.getX()+","+move.getY());
        }
    }
    private void runSingleGame(Agent agent, int edgeSize, double mineFreq, boolean visual) {
        Game game = new Game(edgeSize, mineFreq);
        runSingleGame(agent,game,visual);
    }
    private void runSingleGame(Agent agent, int edgeSize, double mineFreq) {
        runSingleGame(agent,edgeSize,mineFreq,false);
    }

    private static void printHelp() {
        System.out.print(HELP);
    }
}
