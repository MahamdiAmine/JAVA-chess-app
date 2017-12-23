package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

/**
 * Created By Mahamdi Amine on 10/26/17
 */
public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    public Piece(final int piecePosition, final Alliance pieceAlliance, final PieceType pieceType) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        //TODO  a lot of work to do !!!
        this.isFirstMove = false;
        this.cachedHashCode = computeCachedCode();
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public int getPiecePosition() {
        return piecePosition;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    private int computeCachedCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 0 : 1);
        return result;
    }

    public enum PieceType {

        PAWN("P"),
        KNIGHT("H"),
        BISHOP("B"),
        QUEEN("Q"),
        KING("K"),
        ROOK("R");

        private String pieceName;

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;

        }

    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof Piece)) return false;
        final Piece otherPiece = (Piece) other;
        return (otherPiece.getPieceAlliance() == this.pieceAlliance && this.piecePosition == otherPiece.getPiecePosition()
                && pieceType == otherPiece.getPieceType() && this.isFirstMove == otherPiece.isFirstMove());
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;

    }

}
