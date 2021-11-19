package com.mthree.restgame.dao;

import com.mthree.restgame.dto.Game;

import java.util.List;

public interface GameDAO {
    List<Game> getGames();
    Game getGame(int gameId);
    Game insertGame(Game game);
    void updateGame(Game game);
    void deleteGameById(int gameId);
}
