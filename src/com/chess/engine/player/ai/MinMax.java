package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

/**
 * Created By Mahamdi Amine on 12/26/17
 */
public class MinMax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;

    public MinMax(final int searchDepth) {

        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth=searchDepth;
    }

    @Override
    public Move execute(Board board) {
        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.getCurrentPlayer().toString() + " thinking with depth =" + searchDepth);
        int numMoves = board.getCurrentPlayer().getLegalMoves().size();
        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = board.getCurrentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getTransitionBoard(), searchDepth - 1) :
                        max(moveTransition.getTransitionBoard(), searchDepth- 1);
                if (board.getCurrentPlayer().getAlliance().isWhite() && currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.getCurrentPlayer().getAlliance().isBlack() && currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        final long executionTime = System.currentTimeMillis() - startTime;
        return bestMove;
    }

    public int min(final Board board, final int depth) {
        if (depth == 0 || isEndGameScenario(board)) return boardEvaluator.evaluate(board, 0);
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue < lowestSeenValue) lowestSeenValue = currentValue;
            }
        }
        return lowestSeenValue;
    }

    private static boolean isEndGameScenario(final Board board) {

        return board.getCurrentPlayer().isInCheck() ||
                board.getCurrentPlayer().isInStaleMate();
    }

    public int max(final Board board, final int depth) {
        if (depth == 0 || isEndGameScenario(board)) return boardEvaluator.evaluate(board, 0);
        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue > highestSeenValue) highestSeenValue = currentValue;
            }
        }
        return highestSeenValue;
    }

    @Override
    public String toString() {
        return "MinMax";
    }


}
