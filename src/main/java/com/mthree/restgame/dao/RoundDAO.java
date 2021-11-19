package com.mthree.restgame.dao;

import com.mthree.restgame.dto.Round;

import java.util.List;

public interface RoundDAO {
    List<Round> getRoundsByGame(int gameId);
    Round getRound(int roundId);
    Round saveRound(Round round);
    void deleteRoundById(int roundId);
}
