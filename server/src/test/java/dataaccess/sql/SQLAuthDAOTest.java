package dataaccess.sql;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;

public class SQLAuthDAOTest {
    private static AuthDAO authDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        authDAO = new SQLAuthDAO();
    }

    @BeforeEach
    public void setUp() throws DataAccessException{
        authDAO.clear();
    }

    @Test
    @DisplayName("Positive Create & Get Auth Test")
    public void createAuthSuccess() throws DataAccessException{
        AuthData auth = new AuthData("test-token-123", "Amelia");
        authDAO.createAuth(auth);

        AuthData retrieved = authDAO.getAuth("test-token-123");
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("Amelia", retrieved.username());
    }

    @Test
    @DisplayName("Negative Get Auth Test (Bad Token)")
    public void getAuthFail() throws DataAccessException{
        AuthData retrieved = authDAO.getAuth("fake-token");
        Assertions.assertNull(retrieved);
    }

    @Test
    @DisplayName("Positive Delete Auth Test")
    public void deleteAuthSuccess() throws DataAccessException{
        AuthData auth = new AuthData("test-token-123", "Amelia");
        authDAO.createAuth(auth);

        authDAO.deleteAuth("test-token-123");
        Assertions.assertNull(authDAO.getAuth("test-token-123"));
    }
 }
