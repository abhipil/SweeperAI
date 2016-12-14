package io.github.abhipil.sweeper_ai.game;

/**
 * @author abhishek
 *         created on 12/12/16.
 */
public abstract class NeighbourExecutor {
    public void iterate(Square square, int edgeSize) {
        int i = square.getX(), j = square.getY();
        for (int x= i-1; x <i+2;x++) {
            if (x < 0 || x > edgeSize - 1) {
                continue;
            }
            for (int y = j - 1; y < j + 2; y++) {
                if ((y < 0 || y > edgeSize - 1) || (x == i && y == j)) {
                    continue;
                }
                if (!keepExecuting(x, y)) {
                    return;
                }
            }
        }
    }

    protected abstract boolean keepExecuting(int i, int j);
}
