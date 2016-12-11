package io.github.abhipil.sweeper_ai.game;

/**
 * @author abhishek
 *         created on 12/10/16.
 */
public class Position {
    private final int x, y;

    public Position(int xPos, int yPos) {
        x=xPos;
        y=yPos;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
