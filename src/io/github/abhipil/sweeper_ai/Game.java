package io.github.abhipil.sweeper_ai;

import io.github.abhipil.sweeper_ai.game.Square;

import java.util.*;

/**
 * Created by jay on 12/10/16.
 */
public class Game {
    private final Square[][] board;
    private boolean gameOver = false;


    // constructor
    public Game(int edgeSize, int bombFreq) {
        board = new Square[edgeSize][edgeSize];
        // load random bombs
        // load number of  neighbouring mines
    }

    // check if game over
    public boolean isOver() {
        return false;
    }

    public void leftClick(int xPos, int yPos) {
        if (board[xPos][yPos].isMine()) {
            gameOver = true;
        } else if (board[xPos][yPos].getNeighbouringMines() == 0) {
            expandZeroes(xPos, yPos);
        } else {
            if (!board[xPos][yPos].isRevealed()) {
                board[xPos][yPos].reveal();
            }
        }
    }

    private void expandZeroes(int xPos, int yPos) {
        Set<Square> visited = new HashSet<>();
        Queue<Square> queue = new LinkedList<>();
        queue.offer(board[xPos][yPos]);
        visited.add(queue.peek());
        while (!queue.isEmpty()) {
            Square current = queue.poll();
            current.reveal();
            if (current.getNeighbouringMines() != 0) {
                continue;
            }
            int x = current.getX(), y = current.getY();
            for (int i = x-1; i<3; i++) {
                for (int j = y-1; j <3 ; j++) {
                    if (i<0 || i==edgeSize-1) {
                        continue;
                    }
                    if (j<0 || j==edgeSize-1) {
                        continue;
                    }
                    Square child = board[i][j];
                    if (!visited.contains(child)) {
                        queue.offer(child);
                        visited.add(child);
                    }
                }
            }
        }
    }

    public void get(int xPos, int yPos) {

    }
}
