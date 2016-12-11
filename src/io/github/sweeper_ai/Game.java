package io.github.sweeper_ai;

import io.github.sweeper_ai.game.Square;

/**
 * Created by jay on 12/10/16.
 */
public class Game {
    private final Square[][] board;


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

    }
    
    public void get(int xPos, int yPos) {

    }
}
