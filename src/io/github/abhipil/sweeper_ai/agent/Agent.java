package io.github.abhipil.sweeper_ai.agent;

import io.github.abhipil.sweeper_ai.Game;
import io.github.abhipil.sweeper_ai.game.Position;

/**
 * @author abhishek
 *         created on 12/10/16.
 */
public interface Agent {

    Position getMove(Game game);
}
