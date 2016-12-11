package io.github.abhipil.sweeper_ai.game;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jay on 12/10/16.
 */
public class Square {
    private static final int DEFAULT_HASH = 31;

    private final Position position;
    private final boolean mine;

    private final AtomicInteger neighbouringMines;
    private final AtomicBoolean revealed;

    public Square(Position position, boolean mine) {
        this.position = position;
        this.mine = mine;
        neighbouringMines = new AtomicInteger(0);
        revealed = new AtomicBoolean(false);
    }

    public void addNeighbour(){
        if (!isMine()) {
            neighbouringMines.incrementAndGet();
        }
    }

    public int getNeighbouringMines() {
        if (!isMine())
            return neighbouringMines.get();
        throw new IllegalStateException("Square is a mine, does not have the concept of neighbours");
    }

    public boolean isMine() {
        return mine;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public void reveal() {
        revealed.set(true);
    }

    public boolean isRevealed() {
        return revealed.get();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        Square that = (Square) o;
        if (this.getX() != that.getX() || this.getY() != that.getY()) {
            return false;
        }
        if (this.isMine() != that.isMine()) {
            return false;
        }
        if (!isMine() && this.getNeighbouringMines() != this.getNeighbouringMines()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = DEFAULT_HASH;
        hash += DEFAULT_HASH*((Integer) getX()).hashCode();
        hash += DEFAULT_HASH*((Integer) getY()).hashCode();
        hash += DEFAULT_HASH*((Boolean) isMine()).hashCode();
        if (!isMine()) {
            hash += DEFAULT_HASH*((Integer) getNeighbouringMines()).hashCode();
        }
        return hash;
    }
}
