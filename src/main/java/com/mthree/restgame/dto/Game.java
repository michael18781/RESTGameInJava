package com.mthree.restgame.dto;

public class Game implements Cloneable {
    private int gameId;
    private Integer correctAnswer;
    private boolean isFinished;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    @Override
    public boolean equals(Object o){
        if(o == this) return true;
        if(!(o instanceof Game)) return false;
        Game g = (Game) o;
        return (g.getGameId() == this.getGameId()
                && g.getCorrectAnswer() == this.getCorrectAnswer()
                && g.isFinished() == this.isFinished());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
