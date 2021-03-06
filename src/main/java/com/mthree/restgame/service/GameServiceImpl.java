package com.mthree.restgame.service;

import com.mthree.restgame.GameFinishedException;
import com.mthree.restgame.dao.GameDAO;
import com.mthree.restgame.dao.RoundDAO;
import com.mthree.restgame.dto.Game;
import com.mthree.restgame.dto.Round;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GameServiceImpl implements GameService {
    private final static int NUM_DIGITS = 4;

    private final GameDAO gameDao;
    private final RoundDAO roundDao;
    private final Random rand;

    public GameServiceImpl(GameDAO gameDao, RoundDAO roundDao){
        this.roundDao = roundDao;
        this.gameDao = gameDao;
        this.rand = new Random();
    }

    @Override
    public Game censorGame(Game g) {
        // Creating and initialising attributes for a new Game object so the original is not overridden
        Game censoredGame = g;

        if (!g.isFinished()) {
            censoredGame = new Game();
            censoredGame.setGameId(g.getGameId());
            censoredGame.setFinished(g.isFinished());
            censoredGame.setCorrectAnswer(-1);
        }
        return censoredGame;
    }

    @Override
    public Game beginGame() {
        Game newGame = new Game();
        int newAnswer = genAnswer();

        // Repeatedly generate an answer until we get one with unique digits
        while (NotUniqueDigits(newAnswer)) {
            newAnswer = genAnswer();
        }

        newGame.setCorrectAnswer(newAnswer);
        newGame.setFinished(false);
        return gameDao.insertGame(newGame);
    }

    @Override
    public int genAnswer(){
        // Method to compute a random number of certain number of digits
        return (int) (Math.pow(10, NUM_DIGITS - 1) + rand.nextInt((int) (9 * Math.pow(10, NUM_DIGITS - 1))));
    }

    @Override
    public Round makeGuess(int guess, int gameId) {
        Game gameToGuessOn = gameDao.getGame(gameId);

        if (gameToGuessOn.isFinished()){
            throw new GameFinishedException("Game already finished! No more guesses allowed!");
        }

        if (String.valueOf(guess).length() != NUM_DIGITS){
            throw new NumberFormatException("Wrong number of digits in your guess...");
        }

        // Checking for uniqueness of guess i.e. each digit is different
        if(NotUniqueDigits(guess)){
            throw new NumberFormatException("Duplicate digits in your guess. Please make another!");
        }

        // Creating the round with initial data
        Round newRound = new Round();
        newRound.setGameId(gameId);
        newRound.setRoundStarted(Timestamp.valueOf(LocalDateTime.now()));

        // Calculating and setting the number of exact and partial matches for a given round.
        Map<String, Integer> results = computeMatches(guess, gameToGuessOn.getCorrectAnswer());
        int exactMatches = results.get("EXACT");
        int partialMatches = results.get("PARTIAL");

        // Setting the result
        newRound.setResult(String.format("e:%s:p:%s", exactMatches, partialMatches));

        // Setting the right round number for the round
        newRound.setRoundNumber(1 + roundDao.getRoundsByGame(gameToGuessOn.getGameId()).size());

        // Check to see if we have guessed correctly!
        if (exactMatches == NUM_DIGITS){
            gameToGuessOn.setFinished(true);
            gameDao.updateGame(gameToGuessOn);
            throw new GameFinishedException("You have finished the game! Well done.");
        }

        return roundDao.saveRound(newRound);
    }

    @Override
    public List<Game> findAllGames(){
        return gameDao.getGames();
    }

    @Override
    public Game findGameById(int gameId) {
        return gameDao.getGame(gameId);
    }

    @Override
    public List<Round> findRoundsByGameId(int gameId) {
        return roundDao.getRoundsByGame(gameId);
    }

    @Override
    public Map<String, Integer> computeMatches(int guess, int correct){
        int exact = 0;
        int partials = 0;

        // Creating our ArrayList's of digits for the answer and the guess
        ArrayList<Character> correctChars = new ArrayList<>();
        for(char c: Integer.toString(correct).toCharArray()) correctChars.add(c);

        ArrayList<Character> guessChars = new ArrayList<>();
        for(char c: Integer.toString(guess).toCharArray()) guessChars.add(c);

        for(int i = 0; i < guessChars.size(); i++){
            // Check if it is still in the correct answer.
            if (correctChars.contains(guessChars.get(i))) partials++;

            // Check if there is an exact match
            if(correctChars.get(i) == guessChars.get(i)) exact++; partials--;
        }

        // Create a hashmap to store the results of the game to be written out by the service
        Map<String, Integer> results = new HashMap<>();
        results.put("EXACT", exact);
        results.put("PARTIAL", partials);
        return results;
    }

    private boolean NotUniqueDigits(int guess){
        // Check that all the digits are unique in the users guess.
        HashSet<Integer> seen = new HashSet<>();
        int val = guess;

        while(val > 0){
            int digit = val % 10;
            // Found a digit to be repeated hence throw an exception
            if (seen.contains(digit)) return true;

            // Found a new digit so add to digit set
            else seen.add(digit); val /= 10;
        }
        return false;
    }
}
