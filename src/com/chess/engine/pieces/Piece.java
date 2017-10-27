package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.List;

/**
 * Created By Mahamdi Amine on 10/26/17
 */
public abstract class Piece {
    protected final int piecePosition;
    private final Alliance pieceAlliance;

    public Piece(final int piecePosition, final Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
    }
    public abstract List<Move> calculateLegalmoves(final Board board) ;

}
