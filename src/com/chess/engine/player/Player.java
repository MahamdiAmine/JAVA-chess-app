package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

import java.util.Collection;

/**
 * Created By Mahamdi Amine on 11/4/17
 */
public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;

    public Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
    }

    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece instanceof King) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Board without a King !! is this a chess ?? ");
    }

    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    ///TODO implement these methods bellow :

    public boolean isInCheck() {
        return false;
    }


    public boolean isInCheckMate() {
        return false;
    }


    public boolean isInStaleMate() {
        return false;
    }

    public boolean isCastled(){
        return false;
    }
    public MoveTransition makeMove(final Move move){
        return null;
    }

    protected abstract Collection<Piece> getActivePieces();

    public abstract Alliance getAlliance();

    public abstract Player getOpponent();
}
