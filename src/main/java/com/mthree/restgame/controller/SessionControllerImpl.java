package com.mthree.restgame.controller;

import com.mthree.restgame.dto.Game;
import com.mthree.restgame.dto.Round;
import com.mthree.restgame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class SessionControllerImpl implements SessionController {
    @Autowired
    private GameService service;

    @PostMapping("/begin")
    @Override
    public ResponseEntity<Game> begin(){
        Game newGame = service.beginGame();
        return new ResponseEntity<>(service.censorGame(newGame), HttpStatus.CREATED);
    }

    @PostMapping("/guess")
    @Override
    public ResponseEntity<Round> guess(@RequestParam("guess") int guess, @RequestParam("gameId") int gameId){
        return new ResponseEntity<>(service.makeGuess(guess, gameId), HttpStatus.ACCEPTED);
    }

    @GetMapping("/game")
    @Override
    public ResponseEntity<List<Game>> game() {
        List<Game> games = service.findAllGames();
        // Loop to set "correctAnswer" to zero if the game is not finished
        // so that the user can't cheat...
        games.forEach(service::censorGame);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/game/{gameId}")
    @Override
    public ResponseEntity<Game> gameById(@PathVariable("gameId") int gameId) {
        Game gameFound = service.findGameById(gameId);
        System.out.println(service.censorGame(gameFound));
        System.out.println(gameFound);

        // Again, checking if we have finished otherwise displaying 0 for the answer
        return new ResponseEntity<>(service.censorGame(gameFound), HttpStatus.OK);
    }

    @GetMapping("/rounds/{gameId}")
    @Override
    public ResponseEntity<List<Round>> roundsInGame(@PathVariable("gameId") int gameId) {
        return new ResponseEntity<>(service.findRoundsByGameId(gameId), HttpStatus.OK);
    }
}
