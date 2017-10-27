package com.chess.engine;

/**
 * Created By Mahamdi Amine on 10/27/17
 */
public enum Alliance {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }
    };


    public abstract int getDirection();
    }

