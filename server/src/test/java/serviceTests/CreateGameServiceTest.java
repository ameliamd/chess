package serviceTests;

import dataaccess.*;
import model.AuthData;
import model.CreateGameRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import service.CreateGameService;
import service.RegisterService;

public class CreateGameServiceTest {

    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private CreateGameService createGameService;
    private RegisterService registerService;

    @BeforeEach
    public void setup(){
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        createGameService = new CreateGameService(authDAO, gameDAO);
        registerService = new RegisterService(userDAO, authDAO);
    }

    @Test
    @DisplayName("Positive Create Game Test")
    public void createGameSuccess() throws DataAccessException{
        AuthData authData = registerService.register(new UserData("Amelia", "password", "email"));
        int gameID = createGameService.createGame(authData.authToken(), new CreateGameRequest("Amelia's Game"));

        Assertions.assertTrue(gameID > 0);
    }

    @Test
    @DisplayName("Negative Create Game Test (Bad Auth Token)")
    public void createGameFail() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            createGameService.createGame("fake-token", new CreateGameRequest("Bad Game"));
        });
    }
}
