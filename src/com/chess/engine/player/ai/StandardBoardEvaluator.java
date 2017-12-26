package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

/**
 * Created By Mahamdi Amine on 12/26/17
 */
public final class StandardBoardEvaluator implements BoardEvaluator {
    private static final int FACTOR = 1;
    private static final int CHECK_BONUS = 18;
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 6;

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.whitePlayer(), depth) - scorePlayer(board, board.blackPlayer(), depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {

        return pieceValue(player) + mobility(player) +
                check(player) + checkMate(player, depth) +
                castled(player);

    }

    private static int castled(final Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    private static int checkMate(final Player player, final int depth) {
        return player.getOpponent().isInCheck() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
    }

    private static int depthBonus(final int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int check(final Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int mobility(final Player player) {

        return player.getLegalMoves().size() * FACTOR;
    }

    private static int pieceValue(final Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPieceValue();
        }
        return pieceValueScore;
    }
}
