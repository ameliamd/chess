package serviceTests;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import service.ClearService;

public class ClearServiceTest {

    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private ClearService clearService;

    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    @Test
    @DisplayName("Positive Clear Test")
    public void clearSuccess() throws DataAccessException {
        userDAO.createUser(new UserData("Amelia", "pass", "email"));
        clearService.clearAll();
        Assertions.assertNull(userDAO.getUser("Amelia"));
    }
}