package com.mthree.restgame.dao;

import com.mthree.restgame.dto.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoundDAOImpl implements RoundDAO {
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<Round> getRoundsByGame(int gameId) {
        return jdbc.query("SELECT * FROM Round WHERE gameId = ? ORDER BY roundStarted", new RoundMapper(), gameId);
    }

    @Override
    public Round getRound(int roundId) {
        return jdbc.queryForObject("SELECT * FROM Round WHERE roundId = ?", new RoundMapper(), roundId);
    }

    @Override
    public Round saveRound(Round round) {
        jdbc.update("INSERT INTO Round (roundId, gameId, roundNumber, roundStarted, result) VALUES (?, ?, ?, ?, ?)",
                round.getRoundId(),
                round.getGameId(),
                round.getRoundNumber(),
                round.getRoundStarted(),
                round.getResult());

        Round insertedRound = jdbc.queryForObject("SELECT * FROM Round ORDER BY roundId DESC LIMIT 1", new RoundMapper());
        return insertedRound;
    }

    @Override
    public void deleteRoundById(int roundId){
        jdbc.update("DELETE FROM Round WHERE roundId = ?", roundId);
    }

}
