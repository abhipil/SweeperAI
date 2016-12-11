package io.github.abhipil.sweeper_ai.agent;

import io.github.abhipil.sweeper_ai.Game;
import io.github.abhipil.sweeper_ai.game.Position;

import java.util.Scanner;

/**
 * @author abhishek
 *         created on 12/11/16.
 */
public class HumanAgent implements Agent {
    public static final String USAGE = "Usage - x y\n" +
            "\t x - row index" +
            "\t y - column index";

    Scanner in = new Scanner(System.in);
    @Override
    public Position getMove(Game game) {
        String input = in.nextLine();
        int x, y;
        if(input != null) {
            String[] vals = input.trim().split(" ");
            try {
                x = Integer.parseInt(vals[0]);
                y = Integer.parseInt(vals[1]);
                return new Position(x, y);
            } catch (NumberFormatException nfe) {
                printUsage();
                return getMove(game);
            }
        } else {
            return getMove(game);
        }
    }

    public void printUsage() {
        System.out.println(USAGE);
    }
}
