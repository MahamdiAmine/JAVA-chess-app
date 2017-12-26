package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

/**
 * Created By Mahamdi Amine on 12/25/17
 */
public interface MoveStrategy {
    Move execute(Board board, int depth);
}
