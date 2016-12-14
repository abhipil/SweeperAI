package io.github.abhipil.sweeper_ai.agent;

import io.github.abhipil.sweeper_ai.Game;
import io.github.abhipil.sweeper_ai.game.Position;

/**
 * @author abhishek
 *         created on 12/13/16.
 */
public class Node {
    private final Game game;
    private final Position rootMove;
    private final int numMovesMade;

    public Node(Game game, Position rootMove, int numMovesMade) {
        this.game = game;
        this.rootMove = rootMove;
        this.numMovesMade = numMovesMade;
    }

    public Position getRootMove() {
        return rootMove;
    }

    public Game getGame() {
        return game;
    }

    public int getNumMovesMade() {
        return numMovesMade;
    }
}
