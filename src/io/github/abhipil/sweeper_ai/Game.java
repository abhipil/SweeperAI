package io.github.abhipil.sweeper_ai;

import io.github.abhipil.sweeper_ai.game.NeighbourExecutor;
import io.github.abhipil.sweeper_ai.game.Position;
import io.github.abhipil.sweeper_ai.game.Square;

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
    private final Random rand = new Random(System.currentTimeMillis());

    // constructor
    public Game(int edgeSize, double mineFreq) {
        this(edgeSize, mineFreq, (long) ((edgeSize * edgeSize) * mineFreq),
                new ArrayList<Square>(), 0, false);

        for (int i = 0; i < edgeSize; i++) {
            for (int j = 0; j < edgeSize; j++) {
                board[i][j] = new Square(new Position(i,j), false);
            }
        }
        generateMines();
    }

    /* constructor for copy()
     * board is iterated over and copied
     *
     */
    private Game(int edgeSize, double mineFreq, long numOfMines , Square[][] board, List<Square> mines, long numRevealed, boolean gameOver){
        this(edgeSize, mineFreq, numOfMines, mines, numRevealed, gameOver);
        for (int i = 0; i < edgeSize; i++) {
            for (int j = 0; j < edgeSize; j++) {
                this.board[i][j] = new Square(board[i][j]);
            }
        }
    }

    private Game(int edgeSize, double mineFreq, long numOfMines , List<Square> mines, long numRevealed, boolean gameOver){

        this.edgeSize = edgeSize;
        this.mineFreq = mineFreq;
        this.numOfMines = numOfMines;
        this.board = new Square[this.edgeSize][this.edgeSize];
        this.mines = new ArrayList<>();
        for (Square mine : mines) {
            this.mines.add(new Square(mine));
        }
        this.numRevealed = new AtomicLong(numRevealed);
        this.gameOver = new AtomicBoolean(gameOver);
    }

    public Game copy() {
        return new Game(getEdgeSize(),
                this.mineFreq,
                this.numOfMines,
                this.board,
                this.mines,
                this.numRevealed.get(),
                this.gameOver.get());
    }

    private void generateMines() {
        int rCol,rRow;

        for(int index = 0 ; index < numOfMines ; index++ ) {
            rCol = rand.nextInt( edgeSize );
            rRow = rand.nextInt( edgeSize );
            if (board[rRow][rCol].isMine()) {
                index--;
            } else {
                board[rRow][rCol] = new Square(new Position(rRow,rCol),true);
                mines.add(board[rRow][rCol]);

                (new NeighbourExecutor() {
                    @Override
                    protected boolean keepExecuting(int i, int j) {
                        board[i][j].addNeighbour();
                        return true;
                    }
                }).iterate(board[rRow][rCol], edgeSize);
            }
        }
    }



    // check if game over
    public boolean isOver() {
        return gameOver.get();
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
        if (isWon()) {
            for (Square mine : mines) {
                mine.reveal();
            }
            gameOver.set(true);
        }

    }

    private void expandZeroes(int xPos, int yPos) {
        final Set<Square> visited = new HashSet<>();
        final Queue<Square> queue = new LinkedList<>();
        queue.offer(board[xPos][yPos]);
        while (!queue.isEmpty()) {
            final Square current = queue.poll();
            if (!current.isRevealed()) {
                current.reveal();
                numRevealed.incrementAndGet();
            }
            visited.add(current);
            if (current.getNeighbouringMines() != 0) {
                continue;
            }
            (new NeighbourExecutor() {
                @Override
                protected boolean keepExecuting(int i, int j) {
                    Square child = board[i][j];
                    if (!visited.contains(child)) {
                        queue.offer(child);
                    }
                    return true;
                }
            }).iterate(current, edgeSize);
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
