package com.chess.engine.player.ai;

import com.chess.engine.board.Board;

/**
 * Created By Mahamdi Amine on 12/26/17
 */
public interface BoardEvaluator {
    int evaluate(Board board, int depth);
}
