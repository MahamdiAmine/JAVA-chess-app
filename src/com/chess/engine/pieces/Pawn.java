package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;
import static com.chess.engine.board.Move.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created By Mahamdi Amine on 10/27/17
 */
public class Pawn extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {8};

    public Pawn(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + (currentCandidateOffset) * (this.getPieceAlliance().getDirection());
            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if (currentCandidateOffset==8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                // TODO more work to do here !!!
                legalMoves.add(new MajorMove(board,this,candidateDestinationCoordinate));
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Alliance getPieceAlliance() {
        return super.getPieceAlliance();
    }
}
