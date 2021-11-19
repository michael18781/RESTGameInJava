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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestApplicationConfiguration.class)
public class GameDAOTest {
    @Autowired
    GameDAO gameDao;

    @Autowired
    RoundDAO roundDao;

    Game game1;
    Game game2;

    @Before
    public void setUp(){
        removeAllFromDB(gameDao, roundDao);
        game1 = new Game();
        game1.setCorrectAnswer(1234);
        game1.setFinished(false);
        game1 = gameDao.insertGame(game1);

        game2 = new Game();
        game2.setCorrectAnswer(5678);
        game2.setFinished(false);
        game2 = gameDao.insertGame(game2);
    }

    @Test
    public void insertTest(){
        // Ensure that the Game object retrieved is as expected
        Game fromDao = gameDao.getGame(game1.getGameId());
        assertEquals(game1, fromDao);
    }

    @Test
    public void updateTest(){
        // Now perform update
        game1.setFinished(true);
        Game fromDao = gameDao.getGame(game1.getGameId());

        // Ensure that the DTO is updated BUT NOT IN DATABASE
        assertNotEquals(game1, fromDao);

        // Update the database now...
        gameDao.updateGame(game1);

        // Check that now the object retrieved from database is correct
        fromDao = gameDao.getGame(game1.getGameId());
        assertEquals(game1, fromDao);
    }

    @Test
    public void getAllTest(){
        List<Game> games = gameDao.getGames();
        assertEquals(2, games.size());
        assertTrue(games.contains(game1));
        assertTrue(games.contains(game2));
    }

    @Test
    public void getTest(){
        // We have inserted two games. Now try to extract them
        // based on their Id.
        Game fromDao1 = gameDao.getGame(game1.getGameId());
        Game fromDao2 = gameDao.getGame(game2.getGameId());

        // Check that the extracted games are the ones we put in.
        assertEquals(game1, fromDao1);
        assertEquals(game2, fromDao2);
    }

    public static void removeAllFromDB(GameDAO gameDao, RoundDAO roundDao){
        // Get all the games
        List<Game> games = gameDao.getGames();
        for(Game game: games){
            // Find all rounds for each game
            List<Round> roundsForGame = roundDao.getRoundsByGame(game.getGameId());

            // Delete each round
            for(Round round: roundsForGame){
                roundDao.deleteRoundById(round.getRoundId());
            }

            // Finally, delete the game
            gameDao.deleteGameById(game.getGameId());
        }
    }
}
