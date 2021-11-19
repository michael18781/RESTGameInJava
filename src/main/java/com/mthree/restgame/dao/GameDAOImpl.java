package com.mthree.restgame.dao;

import com.mthree.restgame.dto.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameDAOImpl implements GameDAO {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Game insertGame(Game game) {
        jdbc.update("INSERT INTO Game (isFinished, correctAnswer) VALUES (?, ?)", game.isFinished(), game.getCorrectAnswer());

        // Queries the last inserted game to find the id of it as it is set by the DB when inserted.
        Game justInserted = jdbc.queryForObject("SELECT * FROM Game ORDER BY gameId DESC LIMIT 1", new GameMapper());
        return justInserted;
    }

    @Override
    public void updateGame(Game game){
        jdbc.update("UPDATE Game SET isFinished = ? WHERE gameId = ?", game.isFinished(), game.getGameId());
    }

    @Override
    public List<Game> getGames() {
        return jdbc.query("SELECT * FROM Game", new GameMapper());
    }

    @Override
    public Game getGame(int gameId) {
        return jdbc.queryForObject("SELECT * FROM Game WHERE gameId = ?", new GameMapper(), gameId);
    }

    @Override
    public void deleteGameById(int gameId){
        jdbc.update("DELETE FROM Game WHERE gameId = ?", gameId);
    }
}
