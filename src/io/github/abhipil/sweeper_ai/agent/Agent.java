package io.github.abhipil.sweeper_ai.agent;

import io.github.abhipil.sweeper_ai.Game;
import io.github.abhipil.sweeper_ai.game.Move;

/**
 * @author abhishek
 *         created on 12/10/16.
 */
public interface Agent {

    Move getMove(Game game);
}
