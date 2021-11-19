package com.mthree.restgame.dao;

import com.mthree.restgame.dto.Round;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoundMapper implements RowMapper<Round> {
    @Override
    public Round mapRow(ResultSet rs, int index) throws SQLException {
        Round r = new Round();
        r.setGameId(rs.getInt("gameId"));
        r.setRoundNumber(rs.getInt("roundNumber"));
        r.setResult(rs.getString("result"));
        r.setRoundStarted(rs.getTimestamp("roundStarted"));
        r.setRoundId(rs.getInt("roundId"));
        return r;
    }
}