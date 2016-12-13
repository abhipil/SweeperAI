package io.github.abhipil.sweeper_ai.agent;

import io.github.abhipil.sweeper_ai.Game;
import io.github.abhipil.sweeper_ai.game.Position;
import io.github.abhipil.sweeper_ai.game.Square;

import java.util.*;

/**
 * Created by jay on 12/11/16.
 */
public class AStarAgent implements Agent {

    private Square squarePointer;
    private long bestPredictedScore;

    @Override
    public Position getMove(Game game) {


        int edgeSize = game.getEdgeSize();
        long randomSquares = ((edgeSize * edgeSize) - game.getNumRevealed()) / 2;
        long score;

        for (long i =0; i<randomSquares ; i++) {

            Square square = randomUnrevealedSquare(game);
            Position position = new Position(square.getX(),square.getY());
            Game copy = game.copy();
            copy.leftClick(position);
            score = getScore(copy) + evaluateGameState(copy);
            if (score > bestPredictedScore) {
                score = bestPredictedScore;
                squarePointer = square;
            }

        }

        return new Position(squarePointer.getX(),squarePointer.getY());
    }

    private long getScore(Game copy) {
        return copy.getNumRevealed();
    }

    private Square randomUnrevealedSquare(Game game) {

        Square square;
        while(true) {

            Random rand = new Random();
            int rCol,rRow;
            rCol = rand.nextInt( game.getEdgeSize());
            rRow = rand.nextInt( game.getEdgeSize());

            square = game.get(rRow,rCol);
            if (!square.isRevealed()) {
                break;
            }
        }
        return square;
    }

    private long evaluateGameState (Game copy) {
        long value =0;
        if (!copy.isWon()) {
            value = copy.getNumRevealed() + (long)numOfMinesConfirmed(copy);
        }
        return value;
    }

    private int numOfMinesConfirmed(Game copy) {

        int minesCounter =0;
        int edgeSize = copy.getEdgeSize();
        int revealed =0;
        int total =0;

        for (int i =0;i<edgeSize;i++) {
            for (int j=0;j<edgeSize;j++) {

                Square square = copy.get(i,j);
                int valueCounter = 0;
                ArrayList<Square> neighbourList = new ArrayList<>();

                if (!square.isRevealed()) {

                    int x,y;
                    for (x= i-1; x <i +2;x++) {

                        if (x <0 || x >edgeSize -1) {
                            continue;
                        }

                        for (y=j-1;y<j+2;y++) {

                            if ((y <0 || y>edgeSize-1) || (x==i && y==j)) {
                                continue;
                            }
                            Square neighbour = copy.get(x,y);
                            if (neighbour.isRevealed()) {
                                neighbourList.add(neighbour);
                                valueCounter += neighbour.getNeighbouringMines();
                            }
                        }
                    }

                }

                int neighbourValue=0;
                // checking values for neighbours of neighbour
                for (Square sample : neighbourList) {

                    neighbourValue = sample.getNeighbouringMines();
                    int c = sample.getX();
                    int d = sample.getY();


                    if (!square.isRevealed()) {
                        int x, y;
                        for (x = c - 1; x < c + 2; c++) {
                            if (x < 0 || x > edgeSize - 1) {
                                continue;
                            }
                            for (y = d - 1; y < d + 2; y++) {
                                if ((y < 0 || y > edgeSize - 1) || (x == c && y == d)) {
                                    continue;
                                }
                                Square test = copy.get(x, y);
                                if (test.isRevealed()) {

                                    revealed++;
                                }
                                total++;
                            }
                        }
                    }
                }
                if (revealed == total - neighbourValue) {
                    minesCounter++;
                }
            }
        }
        return minesCounter;
    }
}
