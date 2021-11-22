package com.mthree.restgame.service;

import com.mthree.restgame.dto.Game;
import com.mthree.restgame.dto.Round;

import java.util.List;
import java.util.Map;

public interface GameService {
    Game censorGame(Game g);
    Game beginGame();
    Round makeGuess(int guess, int gameId);
    List<Game> findAllGames();
    Game findGameById(int gameId);
    int genAnswer();
    List<Round> findRoundsByGameId(int gameId);
    Map<String, Integer> computeMatches(int guess, int correct);
}
