package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class RegisterServiceTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private RegisterService registerService;

    @BeforeEach
    public void setup(){
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        registerService = new RegisterService(userDAO, authDAO);
    }

    @Test
    @DisplayName("Positive Register Test")
    public void registerSuccess() throws DataAccessException{
        UserData testUser = new UserData("Amelia", "password123", "amelia@byu.edu");
        AuthData result = registerService.register(testUser);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals("Amelia", result.username());
    }

    @Test
    @DisplayName("Negative Register Test (Duplicate User)")
    public void registerFailDuplicate() throws DataAccessException{
        UserData testUser = new UserData("Amelia", "password123", "amelia@byu.edu");

        registerService.register(testUser);

        Assertions.assertThrows(DataAccessException.class, () -> {
            registerService.register(testUser);
        });
    }
}
