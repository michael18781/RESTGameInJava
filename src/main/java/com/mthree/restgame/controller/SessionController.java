package com.mthree.restgame.controller;

import com.mthree.restgame.dto.Game;
import com.mthree.restgame.dto.Round;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SessionController {
    ResponseEntity<Game> begin();
    ResponseEntity<Round> guess(int guess, int gameId);
    ResponseEntity<List<Game>> game();
    ResponseEntity<Game> gameById(int gameId);
    ResponseEntity<List<Round>> roundsInGame(int gameId);
}
