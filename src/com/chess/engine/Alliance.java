package com.chess.engine;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

/**
 * Created By Mahamdi Amine on 10/27/17
 */
public enum Alliance {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EIGHTH_RANK[position];
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public Player choosePlayer(final BlackPlayer blackPlayer, final WhitePlayer whitePlayer) {
            return whitePlayer;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public Player choosePlayer(final BlackPlayer blackPlayer, final WhitePlayer whitePlayer) {
            return blackPlayer;
        }
    };


    public abstract int getDirection();

    public abstract int getOppositeDirection();

    public abstract boolean isPawnPromotionSquare(int position);

    public abstract boolean isBlack();

    public abstract boolean isWhite();

    public abstract Player choosePlayer(BlackPlayer blackPlayer, WhitePlayer whitePlayer);
}

