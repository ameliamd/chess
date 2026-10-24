package service;

import dataaccess.*;
import model.AuthData;
import model.CreateGameRequest;
import model.JoinGameRequest;
import model.UserData;
import org.junit.jupiter.api.*;

public class JoinGameServiceTest {

    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private JoinGameService joinGameService;
    private CreateGameService createGameService;
    private RegisterService registerService;

    @BeforeEach
    public void setup(){
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        joinGameService = new  JoinGameService(authDAO, gameDAO);
        createGameService = new CreateGameService(authDAO, gameDAO);
        registerService = new RegisterService(userDAO, authDAO);
    }

    @Test
    @DisplayName("Positive Join Game Test")
    public void joinGameSuccess() throws DataAccessException{
        AuthData authData = registerService.register(new UserData("Amelia", "pass", "email"));
        int gameID = createGameService.createGame(authData.authToken(), new CreateGameRequest("Test Ga,e"));

        joinGameService.joinGame(authData.authToken(), new JoinGameRequest("WHITE", gameID));
        Assertions.assertEquals("Amelia", gameDAO.getGame(gameID).whiteUsername());

    }

    @Test
    @DisplayName("Negative Join Game Test (Steal Spot)")
    public void joinGameFailSteal() throws DataAccessException{
        AuthData authData1 = registerService.register(new UserData("Amelia", "password", "email"));
        int gameID = createGameService.createGame(authData1.authToken(), new CreateGameRequest("Test Game"));

        joinGameService.joinGame(authData1.authToken(), new JoinGameRequest("WHITE", gameID));

        AuthData authData2 = registerService.register(new UserData("Intruder", "pass", "email"));
        Assertions.assertThrows(DataAccessException.class, () -> {
            joinGameService.joinGame(authData2.authToken(), new JoinGameRequest("WHITE", gameID));
        });
    }
}
