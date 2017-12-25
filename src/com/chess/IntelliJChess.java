package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.Table;

/**
 * Created By Mahamdi Amine on 11/2/17
 */
public class IntelliJChess {
    public static void main(String[] args) {

        Board board=Board.createStandardBoard();
        System.out.println(board);
        Table table=new Table();
    }

}
