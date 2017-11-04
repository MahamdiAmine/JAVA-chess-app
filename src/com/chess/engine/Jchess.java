package com.chess.engine;

import com.chess.engine.board.Board;

/**
 * Created By Mahamdi Amine on 11/2/17
 */
public class Jchess {
    public static void main(String[] args) {

        Board board=Board.createStandardBoard();
        System.out.println(board);
    }

}
