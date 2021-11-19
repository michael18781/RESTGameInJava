package com.mthree.restgame.dto;

import java.sql.Timestamp;
import java.util.Objects;

public class Round {
    private int roundId;
    private int gameId;
    private int roundNumber;
    private Timestamp roundStarted;
    private String result;

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Timestamp getRoundStarted() {
        return roundStarted;
    }

    public void setRoundStarted(Timestamp roundStarted) {
        this.roundStarted = roundStarted;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return roundId == round.roundId && gameId == round.gameId && roundNumber == round.roundNumber && Objects.equals(roundStarted, round.roundStarted) && Objects.equals(result, round.result);
    }
}
