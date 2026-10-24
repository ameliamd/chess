package serviceTests;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.LogoutService;
import service.RegisterService;

public class LogoutServiceTest {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private LogoutService logoutService;
    private RegisterService registerService;

    @BeforeEach
    public void setup(){
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        logoutService = new LogoutService(authDAO);
        registerService = new RegisterService(userDAO, authDAO);
    }

    @Test
    @DisplayName("Positive Logout Test")
    public void logoutSuccess() throws DataAccessException{
        UserData testUser = new UserData("Amelia", "password123", "amelia@byu.edu");
        AuthData authData = registerService.register(testUser);

        logoutService.logout(authData.authToken());
    }

    @Test
    @DisplayName("Negative Logout Test")
    public void logoutFailBadToken() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            logoutService.logout("fake-bad-token");
        });
    }
}
