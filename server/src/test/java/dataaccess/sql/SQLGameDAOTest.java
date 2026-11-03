package dataaccess.sql;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.Collection;

public class SQLGameDAOTest {
    private static GameDAO gameDAO;

    @BeforeAll
    public static void init() throws DataAccessException{
        gameDAO = new SQLGameDAO();
    }

    @BeforeEach
    public void setUp() throws DataAccessException{
        gameDAO.clear();
    }

    @Test
    @DisplayName("Positive Create & Get Game Test")
    public void createGameSuccess() throws DataAccessException{
        GameData newGame = new GameData(0, null, null, "Amelia's Game", new ChessGame());
        int gameID = gameDAO.createGame(newGame);
        GameData retrieved = gameDAO.getGame(gameID);

        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("Amelia's Game", retrieved.gameName());
    }

    @Test
    @DisplayName("Negative Get Game Test (Bad ID)")
    public void getGameFail() throws DataAccessException {
        GameData retrieved = gameDAO.getGame(9999);
        Assertions.assertNull(retrieved);
    }

    @Test
    @DisplayName("Positive List Game Test")
    public void listGamesSuccess() throws DataAccessException{
        gameDAO.createGame(new GameData(0, null, null, "Game 1", new ChessGame()));
        gameDAO.createGame(new GameData(0, null, null, "Game 2", new ChessGame()));

        Collection<GameData> games = gameDAO.listGames();
        Assertions.assertEquals(2, games.size());
    }

    @Test
    @DisplayName("Positive Update Game Test")
    public void updateGameSuccess() throws DataAccessException{
        int gameId = gameDAO.createGame(new GameData(0, null, null, "Test Game", new ChessGame()));
        GameData original = gameDAO.getGame(gameId);

        GameData updatedGame = new GameData(gameId, "Amelia", original.blackUsername(), original.gameName(), original.game());
        gameDAO.updateGame(updatedGame);

        GameData retrieved = gameDAO.getGame(gameId);
        Assertions.assertEquals("Amelia", retrieved.whiteUsername());
    }
    @Test
    @DisplayName("Negative Update Game Test (Game Doesn't Exist)")
    public void updateGameFail(){
        GameData fakeGame = new GameData(9999, "Amelia", null, "Fake", new ChessGame());

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.updateGame(fakeGame);
        });
    }
}
