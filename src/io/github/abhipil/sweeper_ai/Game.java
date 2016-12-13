package io.github.abhipil.sweeper_ai;

import io.github.abhipil.sweeper_ai.game.Position;
import io.github.abhipil.sweeper_ai.game.Square;
import javafx.geometry.Pos;

import java.util.*;
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

    private final List<Square> mines;
    private final AtomicLong numRevealed;
    private final AtomicBoolean gameOver;



    // constructor
    public Game(int edgeSize, double mineFreq) {
<<<<<<< HEAD
        // initializing edgeSize
        this.edgeSize = edgeSize;
        this.mineFreq = mineFreq;
        numOfMines = (long) ((edgeSize * edgeSize) * mineFreq);

        board = new Square[edgeSize][edgeSize];
=======
        this(edgeSize, mineFreq, (long) ((edgeSize * edgeSize) * mineFreq),
                new ArrayList<Square>(), 0, false);
>>>>>>> ace210f62bab7b472d91885eedb2d88bd858beb6
        for (int i = 0; i < edgeSize; i++) {
            for (int j = 0; j < edgeSize; j++) {
                board[i][j] = new Square(new Position(i,j), false);
            }
        }
        generateMines();
    }

    private Game(int edgeSize, double mineFreq, long numOfMines , List<Square> mines, long numRevealed, boolean gameOver){

        this.edgeSize = edgeSize;
        this.mineFreq = mineFreq;
        this.numOfMines = numOfMines;
        this.board = new Square[this.edgeSize][this.edgeSize];
        this.mines = new ArrayList<>(mines);
        this.numRevealed = new AtomicLong(numRevealed);
        this.gameOver = new AtomicBoolean(gameOver);
    }
    private Game(int edgeSize, double mineFreq, long numOfMines , Square[][] board, List<Square> mines, long numRevealed, boolean gameOver){
        this(edgeSize, mineFreq, numOfMines, mines, numRevealed, gameOver);
        for (int i = 0; i < edgeSize; i++) {
            for (int j = 0; j < edgeSize; j++) {
                this.board[i][j] = board[i][j].copy();
            }
        }
    }

    public Game copy() {
        return new Game(getEdgeSize(),
                mineFreq,
                numOfMines,
                board,
                mines,
                numRevealed.get(),
                gameOver.get());
    }

    private void generateMines() {

        Random rand = new Random();
        int rCol,rRow;

        for(int index = 0 ; index < numOfMines ; index++ ) {
            rCol = rand.nextInt( edgeSize );
            rRow = rand.nextInt( edgeSize );
            if (board[rRow][rCol].isMine()) {
                index--;
            }

            else {
                board[rRow][rCol] = new Square(new Position(rRow,rCol),true);
                mines.add(board[rRow][rCol]);
                if( rRow - 1 >= 0 && rCol - 1 >= 0 )	// upper left square
                    board[rRow - 1][rCol - 1].addNeighbour();
                if( rRow - 1 >= 0 && rCol >= 0 )	// upper middle square
                    board[rRow - 1][rCol].addNeighbour();
                if( rRow - 1 >= 0 && rCol + 1 < edgeSize )	// upper right square
                    board[rRow - 1][rCol + 1].addNeighbour();
                if( rRow >= 0 && rCol - 1 >= 0 )	// middle left square
                    board[rRow][rCol - 1].addNeighbour();
                if( rRow >= 0 && rCol + 1 < edgeSize )	// middle right square
                    board[rRow][rCol + 1].addNeighbour();
                if( rRow + 1 < edgeSize && rCol - 1 >= 0 )	// lower left square
                    board[rRow + 1][rCol - 1].addNeighbour();
                if( rRow + 1 < edgeSize && rCol >= 0 )	// lower middle square
                    board[rRow + 1][rCol].addNeighbour();
                if( rRow + 1 < edgeSize && rCol + 1 < edgeSize )	// lower left square
                    board[rRow + 1][rCol + 1].addNeighbour();

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

    public void leftClick(Position pos) {
        int xPos = pos.getX(), yPos=pos.getY();
        if (board[xPos][yPos].isRevealed()) {
            return;
        }
        if (board[xPos][yPos].isMine()) {
            for (Square mine : mines) {
                mine.reveal();
            }
            gameOver.set(true);
        } else if (board[xPos][yPos].getNeighbouringMines() == 0) {
            expandZeroes(xPos, yPos);
        } else {
            if (!board[xPos][yPos].isRevealed()) {
                board[xPos][yPos].reveal();
                numRevealed.incrementAndGet();
            }
        }
    }

    private void expandZeroes(int xPos, int yPos) {
        System.out.println("Expanding zeroes");
        Set<Square> visited = new HashSet<>();
        Queue<Square> queue = new LinkedList<>();
        queue.offer(board[xPos][yPos]);
        while (!queue.isEmpty()) {
            Square current = queue.poll();
            if (!current.isRevealed()) {
                current.reveal();
                numRevealed.incrementAndGet();
            }
            visited.add(current);
            if (current.getNeighbouringMines() != 0) {
                continue;
            }
            int x = current.getX(), y = current.getY();
            for (int i = x-1; i<edgeSize && i<x+2; i++) {
                if (i<0) {
                    continue;
                }
                for (int j = y-1; j<edgeSize && j<y+2 ; j++) {
                    if (j<0 || (i==x&&j==y) ) {
                        continue;
                    }
                    Square child = board[i][j];
                    if (!visited.contains(child)) {
                        queue.offer(child);
                    }
                }
            }
        }
    }

    public Square get(int xPos, int yPos) {
        return board[xPos][yPos];
    }

    public int getEdgeSize () {
        return edgeSize;
    }

    public long getNumRevealed () {
        return numRevealed.get();
    }


    public void print() {
        StringBuffer sb = new StringBuffer();
        sb.append(' ').append(' ');
        for (int i = 0; i < edgeSize; i++) {
            sb.append(i);
        }
        sb.append("\n  ");
        for (int i = 0; i < edgeSize; i++) {
            sb.append('-');
        }
        sb.append('\n');
        for (int i = 0; i < edgeSize; i++) {
            sb.append(i).append('|');
            for (int j = 0; j < edgeSize; j++) {
                if (board[i][j].isRevealed()) {
                    if (board[i][j].isMine()) {
                        sb.append('*');
                    } else {
                        int neighbours = board[i][j].getNeighbouringMines();
                        if (neighbours == 0) {
                            sb.append(' ');
                        } else {
                            sb.append(neighbours);
                        }
                    }
                } else {
                    sb.append('.');
                }
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }
}
