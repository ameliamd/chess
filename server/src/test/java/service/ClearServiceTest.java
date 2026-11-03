package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;

public class ClearServiceTest {

    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private ClearService clearService;

    @BeforeEach
    public void setup() throws DataAccessException{
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();

        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();

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