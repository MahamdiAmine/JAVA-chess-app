package com.chess.test.board;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.*;
import com.google.common.collect.Iterables;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created By Mahamdi Amine on 12/25/17
 */
public class BoardTest {
    @Test
    public void initialBoard() {

        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.getCurrentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
//        assertFalse(board.getCurrentPlayer().isCastled());
//        assertTrue(board.getCurrentPlayer().isKingSideCastleCapable());
//        assertTrue(board.getCurrentPlayer().isQueenSideCastleCapable());
        assertEquals(board.getCurrentPlayer(), board.whitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.blackPlayer());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertEquals(board.getCurrentPlayer().getOpponent().getOpponent(), board.getCurrentPlayer());
//        assertFalse(board.getCurrentPlayer().getOpponent().isCastled());
//        assertTrue(board.getCurrentPlayer().getOpponent().isKingSideCastleCapable());
//        assertTrue(board.getCurrentPlayer().getOpponent().isQueenSideCastleCapable());
        assertTrue(board.whitePlayer().toString().equals("White"));
        assertTrue(board.blackPlayer().toString().equals("Black"));

//        final Iterable<Piece> allPieces = board.getAllPieces();
        final Iterable<Move> allMoves = Iterables.concat(board.whitePlayer().getLegalMoves(), board.blackPlayer().getLegalMoves());
        for (final Move move : allMoves) {
            assertFalse(move.isAttack());
            assertFalse(move.isCastlingMove());
//            assertEquals(MoveUtils.exchangeScore(move), 1);
        }

    }

    @Test(expected = RuntimeException.class)
    public void testInvalidBoard() {

        final Board.Builder builder = new Board.Builder();

        //Black layout
        //***********************************************************
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));
        //***********************************************************
        //White layout
        //***********************************************************
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Rook(63, Alliance.WHITE));
        builder.setPiece(new Pawn(48, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        //***********************************************************
        //White to move
        builder.setMoveMaker(Alliance.WHITE);
    }
    @Test
    public void testAlgebreicNotation() {
        assertEquals(BoardUtils.getPositionAtCoordinate(0), "a8");
        assertEquals(BoardUtils.getPositionAtCoordinate(1), "b8");
        assertEquals(BoardUtils.getPositionAtCoordinate(2), "c8");
        assertEquals(BoardUtils.getPositionAtCoordinate(3), "d8");
        assertEquals(BoardUtils.getPositionAtCoordinate(4), "e8");
        assertEquals(BoardUtils.getPositionAtCoordinate(5), "f8");
        assertEquals(BoardUtils.getPositionAtCoordinate(6), "g8");
        assertEquals(BoardUtils.getPositionAtCoordinate(7), "h8");
    }

//    @Test
//    public void tt() {
//
//        final Board.Builder builder = new Board.Builder();
//        //BLACK LAYOUT
//        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
//        //WHITE LAYOUT
//        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
//        builder.setPiece(new Bishop(Alliance.WHITE, 61));
//        //white to move
//        builder.setMoveMaker(Alliance.WHITE);
//        final Board board = builder.build();
//        System.out.println(FenUtilities.createFENFromGame(board));
//    }
}