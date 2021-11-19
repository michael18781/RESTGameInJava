import com.mthree.restgame.TestApplicationConfiguration;
import com.mthree.restgame.dao.GameDAO;
import com.mthree.restgame.dao.RoundDAO;
import com.mthree.restgame.dto.Game;
import com.mthree.restgame.dto.Round;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestApplicationConfiguration.class)
public class RoundDAOTest {
    @Autowired
    GameDAO gameDao;

    @Autowired
    RoundDAO roundDao;

    @Before
    public void setUp(){
        GameDAOTest.removeAllFromDB(gameDao, roundDao);
    }

    @After
    public void finish(){
        GameDAOTest.removeAllFromDB(gameDao, roundDao);
    }

    @Test
    public void saveTest(){
        // Test to check rounds are saved correctly
        Game game = new Game();
        game.setCorrectAnswer(1234);
        game.setFinished(false);
        game = gameDao.insertGame(game);

        // Create a round and insert into DB
        Round round = new Round();
        round.setGameId(game.getGameId());
        round.setRoundStarted(Timestamp.valueOf(LocalDateTime.now()));
        round.setResult("e:2:p:1");
        round = roundDao.saveRound(round);

        Round fromDao = roundDao.getRound(round.getRoundId());
        assertEquals(round, fromDao);
    }

    @Test
    public void deleteByIdTest(){
        // Test to check delete of rounds by Id
        Game game = new Game();
        game.setCorrectAnswer(1234);
        game.setFinished(false);
        game = gameDao.insertGame(game);

        // Create a round and insert into DB
        Round round = new Round();
        round.setGameId(game.getGameId());
        round.setRoundStarted(Timestamp.valueOf(LocalDateTime.now()));
        round.setResult("e:2:p:1");
        round = roundDao.saveRound(round);

        // Now try to delete the round and hope for an error when no rounds found
        roundDao.deleteRoundById(round.getRoundId());

        int roundIdDeleted = round.getRoundId();
        assertThrows(EmptyResultDataAccessException.class, () -> {
            roundDao.getRound(roundIdDeleted);
        });
    }

    @Test
    public void getAllByGameIdTest(){
        // Test to check all rounds by game can be found
        Game game1 = new Game();
        game1.setCorrectAnswer(1234);
        game1.setFinished(false);
        game1 = gameDao.insertGame(game1);

        Game game2 = new Game();
        game2.setCorrectAnswer(5678);
        game2.setFinished(false);
        game2 = gameDao.insertGame(game2);

        Random rand = new Random();

        // Create a set of rounds for game1
        int NUM_ROUNDS_TEST = 10;
        ArrayList<Round> createdRounds = new ArrayList<>();
        for(int i = 0; i < NUM_ROUNDS_TEST; i++){
            Round gameRound = new Round();
            gameRound.setGameId(game1.getGameId());
            gameRound.setRoundStarted(Timestamp.valueOf(LocalDateTime.now()));
            gameRound.setResult(String.format("e:%s:p:%s", rand.nextInt(4), rand.nextInt(4)));
            createdRounds.add(roundDao.saveRound(gameRound));
        }

        // Add a round for the second game to ensure that not all rounds in the DB
        // are from the same game i.e. when finding all from game1, it should leave
        // out this one.
        Round roundForSecondGame = new Round();
        roundForSecondGame.setGameId(game2.getGameId());
        roundForSecondGame.setRoundStarted(Timestamp.valueOf(LocalDateTime.now()));
        roundForSecondGame.setResult("e:2:p:1");
        roundDao.saveRound(roundForSecondGame);

        // Get all rounds from the DB and ensure size matches what we inserted
        List<Round> retrievedRounds = roundDao.getRoundsByGame(game1.getGameId());
        assertEquals(createdRounds.size(), retrievedRounds.size());

        // Go through every element in the createdRounds and check they are in
        // those received
        for(int i = 0; i < NUM_ROUNDS_TEST; i++){
            assertTrue(retrievedRounds.contains(createdRounds.get(i)));
        }
    }

    @Test
    public void getByIdTest(){
        // Test to check a given round by id is returned
        Game game = new Game();
        game.setCorrectAnswer(1234);
        game.setFinished(false);
        game = gameDao.insertGame(game);

        Round gameRound = new Round();
        gameRound.setGameId(game.getGameId());
        gameRound.setRoundStarted(Timestamp.valueOf(LocalDateTime.now()));
        gameRound.setResult("e:2:p:1");

        // Save it and retrieve the ID
        gameRound = roundDao.saveRound(gameRound);

        Round fromDao = roundDao.getRound(gameRound.getRoundId());
        assertEquals(gameRound, fromDao);
    }

}

