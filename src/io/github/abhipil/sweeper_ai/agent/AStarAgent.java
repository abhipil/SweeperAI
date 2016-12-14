package io.github.abhipil.sweeper_ai.agent;

import io.github.abhipil.sweeper_ai.Game;
import io.github.abhipil.sweeper_ai.game.NeighbourExecutor;
import io.github.abhipil.sweeper_ai.game.Position;
import io.github.abhipil.sweeper_ai.game.Square;

import java.util.*;

/**
 * Created by jay on 12/11/16.
 */

public class AStarAgent implements Agent {

    private final Random rand = new Random(System.currentTimeMillis());
    private final Comparator<Node> comparator = new Comparator<Node>() {
        @Override
        public int compare(Node o1, Node o2) {
            return (int) (getHeuristicScore(o2.getGame())
                    - getHeuristicScore(o1.getGame()));
        }
    };
    private final Queue<Node> maxQueue = new PriorityQueue<>(100, comparator);

    @Override
    public Position getMove(Game game) {
        int edgeSize = game.getEdgeSize();
        long randomSquares = ((edgeSize * edgeSize) - game.getNumRevealed()) / 2;
        Node current = new Node(game.copy(),null, 0);
        Node bestMove = current;
        List<Position> rootSquares = randomUnrevealedSquare(current.getGame());
        for (Position position : rootSquares) {
            if (!maxQueue.isEmpty() && comparator.compare(maxQueue.peek(),bestMove)<0) {
                bestMove = maxQueue.peek();
            }
            current = new Node(game.copy(), position, 0);
            if (bestMove.getRootMove() == null) {
                bestMove = current;
            }
            Game nextGame = current.getGame().copy();
            nextGame.leftClick(position);
            Node next = new Node(nextGame, position, 1);
            maxQueue.offer(next);
        }
        for (long i =0; i<randomSquares && !maxQueue.isEmpty() ; i++) {
            current = maxQueue.poll();
            List<Position> squares = randomUnrevealedSquare(current.getGame());
            if (comparator.compare(current, bestMove)<0) {
                bestMove = current;
            }
            for (Position position : squares) {
                Game next = current.getGame().copy();
                next.leftClick(position);
                maxQueue.offer(new Node(next, current.getRootMove(), current.getNumMovesMade()+1));
            }
        }
        maxQueue.clear();
        return new Position(bestMove.getRootMove().getX(), bestMove.getRootMove().getY());
    }

    private long getScore(Game copy) {
        return copy.getNumRevealed();
    }

    private long getPredictedScore(Game copy) {
        return numOfMinesConfirmed(copy);
    }

    private List<Position> randomUnrevealedSquare(Game game) {
        List<Position> square = new ArrayList<>();
        rand.setSeed(System.currentTimeMillis());
        int edgeSize = game.getEdgeSize();
        for (int i=0; i<edgeSize+1; i++){
            int rCol,rRow;
            rCol = rand.nextInt(game.getEdgeSize());
            rRow = rand.nextInt(game.getEdgeSize());

            Square randSquare = game.get(rRow,rCol);
            if (!randSquare.isRevealed()) {
                square.add(new Position(randSquare.getX(), randSquare.getY()));
            } else {
                i--;
            }
        }
        return square;
    }

    private long numOfMinesConfirmed(final Game copy) {
        final int[] minesCounter = {0};
        final int edgeSize = copy.getEdgeSize();
        final int[] revealed = {0};
        final int[] total = {0};
        if (copy.getNumRevealed() == 0) {
            return 0;
        }
        for (int i =0;i<edgeSize;i++) {
            for (int j = 0; j < edgeSize; j++) {
                Square square = copy.get(i, j);
                if (square.isRevealed()) {
                    continue;
                }
                (new NeighbourExecutor() {
                    @Override
                    protected boolean keepExecuting(int i, int j) {
                        Square neighbour = copy.get(i, j);
                        if (!neighbour.isRevealed()) {
                            return true;
                        }
                        if (neighbour.getNeighbouringMines()==0) {
                            return true;
                        }
                        total[0]=0;
                        revealed[0]=0;
                        (new NeighbourExecutor() {
                            @Override
                            protected boolean keepExecuting(int i, int j) {
                                Square test = copy.get(i, j);
                                if (test.isRevealed()) {
                                    revealed[0]++;
                                }
                                total[0]++;
                                return true;
                            }
                        }).iterate(neighbour, edgeSize);

                        if (revealed[0] == total[0] - neighbour.getNeighbouringMines()) {
                            minesCounter[0]++;
                            return false;
                        }
                        return true;
                    }
                }).iterate(square, edgeSize);
            }
        }
        return minesCounter[0];
    }

    private long getHeuristicScore(Game game) {
        if (game.isOver()) {
            if (game.isWon()) {
                return Long.MAX_VALUE;
            } else {
                return 0;
            }
        }
        return getScore(game) + getPredictedScore(game);
    }
}
