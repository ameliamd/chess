package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class LoginServiceTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private LoginService loginService;
    private RegisterService registerService;

    @BeforeEach
    public void setup(){
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        loginService = new LoginService(userDAO, authDAO);
        registerService = new RegisterService(userDAO, authDAO);
    }

    @Test
    @DisplayName("Positive Login Test")
    public void loginSuccess() throws DataAccessException{
        UserData testUser = new UserData("Amelia", "password123", "amelia@byu.edu");
        registerService.register(testUser);

        AuthData result = loginService.login(new UserData("Amelia", "password123", null));

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals("Amelia", result.username());
    }

    @Test
    @DisplayName("Negative Login Test (Wrong Password)")
    public void loginWrongPassword() throws DataAccessException{
        UserData testUser = new UserData("Amelia", "password123", "amelia@byu.edu");
        registerService.register(testUser);

        Assertions.assertThrows(DataAccessException.class, () -> {
            loginService.login(new UserData("Amelia", "wrongpassword", null));
        });
    }
}
