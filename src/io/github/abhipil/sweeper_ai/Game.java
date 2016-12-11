package io.github.abhipil.sweeper_ai;

import io.github.abhipil.sweeper_ai.game.Square;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jay on 12/10/16.
 */
public class Game {

    private final Square[][] board;
    private final int edgeSize;
    private final double mineFreq;
    private final long numOfMines;

    private final AtomicLong numRevealed;
    private final AtomicBoolean gameOver;


    // constructor
    public Game(int edgeSize, int mineFreq) {
        // initializing edgeSize
        this.edgeSize = edgeSize;
        this.mineFreq = mineFreq;
        numOfMines = (long) (edgeSize * edgeSize) * mineFreq;

        board = new Square[edgeSize][edgeSize];
        generateMines();
        numRevealed = new AtomicLong(0);
        gameOver = new AtomicBoolean(false);
    }

    private void generateMines() {

        Random rand = new Random();
        int rCol,rRow;

        rCol = rand.nextInt( edgeSize );
        rRow = rand.nextInt( edgeSize );

        for(int index = 0 ; index < numOfMines ; index++ ) {

            if (board[rRow][rCol].isMine()) {
                index--;
            }

            else {

                board[rRow][rCol] = new Square(rRow,rCol,true);
                if( rRow - 1 >= 0 && rCol - 1 >= 0 )	// upper left square
                    board[rRow - 1][rCol - 1].addNeighbors( 1 );
                if( rRow - 1 >= 0 && rCol >= 0 )	// upper middle square
                    board[rRow - 1][rCol].addNeighbors( 1 );
                if( rRow - 1 >= 0 && rCol + 1 < edgeSize )	// upper right square
                    board[rRow - 1][rCol + 1].addNeighbors( 1 );
                if( rRow >= 0 && rCol - 1 >= 0 )	// middle left square
                    board[rRow][rCol - 1].addNeighbors( 1 );
                if( rRow >= 0 && rCol + 1 < edgeSize )	// middle right square
                    board[rRow][rCol + 1].addNeighbors( 1 );
                if( rRow + 1 < edgeSize && rCol - 1 >= 0 )	// lower left square
                    board[rRow + 1][rCol - 1].addNeighbors( 1 );
                if( rRow + 1 < edgeSize && rCol >= 0 )	// lower middle square
                    board[rRow + 1][rCol].addNeighbors( 1 );
                if( rRow + 1 < edgeSize && rCol + 1 < edgeSize )	// lower left square
                    board[rRow + 1][rCol + 1].addNeighbors( 1 );

            }
        }
    }



    // check if game over
    public boolean isOver() {
        return isWon() || gameOver.get();
    }

    public boolean isWon() {
        return numRevealed.get() == edgeSize*edgeSize-numOfMines;
    }

    public void leftClick(int xPos, int yPos) {

    }
    
    public Square get(int xPos, int yPos) {

        return board[xPos][yPos];
    }
}
