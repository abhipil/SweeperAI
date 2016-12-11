package io.github.abhipil.sweeper_ai.game;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jay on 12/10/16.
 */
public class Square {
    private final int xPos, yPos;
    private final boolean mine;

    private final AtomicInteger neighbouringMines;
    private final AtomicBoolean revealed;

    public Square(int xPos, int yPos, boolean mine) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.mine = mine;
        neighbouringMines = new AtomicInteger(0);
        revealed = new AtomicBoolean(false);
    }

    public int addNeighbour(){
        return neighbouringMines.incrementAndGet();
    }

    public int getNeighbouringMines() {
        return neighbouringMines.get();
    }

    public boolean isMine() {
        return mine;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public void reveal() {
        revealed.set(true);
    }

    public boolean isRevealed() {
        return revealed.get();
    }
}
