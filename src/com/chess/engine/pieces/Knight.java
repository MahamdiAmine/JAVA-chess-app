package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Mahamdi Amine on 10/27/17
 */
public class Knight extends Piece {
    private final static int[] CANDIDATE_MOVE_CORDINATTES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public List<Move> calculateLegalmoves(Board board) {
        int candidateDestinationCoordinate;
        List<Move> legalMoves=new ArrayList<>();
        for (final int currentCandidate : CANDIDATE_MOVE_CORDINATTES) {
            candidateDestinationCoordinate = this.piecePosition + currentCandidate;
            if(true/*valid coordinate*/){
                final Tile candidateDestinationTile=board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add()
                }
            }
        }
        return null;
    }
}
