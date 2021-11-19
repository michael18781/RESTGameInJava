package com.mthree.restgame.dao;

import com.mthree.restgame.dto.Game;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameMapper implements RowMapper<Game> {
    @Override
    public Game mapRow(ResultSet rs, int index) throws SQLException {
        Game g = new Game();
        g.setGameId(rs.getInt("gameId"));
        g.setCorrectAnswer(rs.getInt("correctAnswer"));
        g.setFinished(rs.getBoolean("isFinished"));
        return g;
    }
}
