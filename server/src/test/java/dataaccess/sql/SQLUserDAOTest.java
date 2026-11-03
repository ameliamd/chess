package dataaccess.sql;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;

public class SQLUserDAOTest {
    private static UserDAO userDAO;

    @BeforeAll
    public static void init() throws DataAccessException{
        userDAO = new SQLUserDAO();
    }

    @BeforeEach
    public void setUp() throws DataAccessException{
        userDAO.clear();
    }
    @Test
    @DisplayName("Positive User Creation Test")
    public void createUserSuccess() throws DataAccessException{
        UserData user = new UserData("Amelia", "password123", "amelia@byu.edu");
        userDAO.createUser(user);

        UserData retrieved = userDAO.getUser("Amelia");
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("Amelia", retrieved.username());

    }

    @Test
    @DisplayName("Negative User Creation Test (Duplicate Username")
    public void createUserFailDuplicate() throws DataAccessException{
        UserData user = new UserData("Amelia", "password123", "amelia@byu.edu");
        userDAO.createUser(user);

        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(user);
        });
    }
}
