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

    public Piece(final int piecePosition,
                 final Alliance pieceAlliance,
                 final PieceType pieceType,
                 final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        //TODO  a lot of work to do !!!
        this.isFirstMove = isFirstMove;
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

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public enum PieceType {

        PAWN("P",100),
        KNIGHT("N",300),
        BISHOP("B",300),
        QUEEN("Q",900),
        KING("K",10000),
        ROOK("R",500);

        private String pieceName;
        private int pieceValue;

        PieceType(final String pieceName,final int pieceValue) {
            this.pieceName = pieceName;
            this.pieceValue=pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;

        }

        public int getPieceValue(){
            return this.pieceValue;
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
