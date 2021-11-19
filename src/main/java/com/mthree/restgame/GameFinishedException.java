package com.mthree.restgame;

public class GameFinishedException extends RuntimeException {
    public GameFinishedException(String msg){
        super(msg);
    }
}
