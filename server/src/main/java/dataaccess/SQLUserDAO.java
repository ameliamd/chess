package dataaccess;

import model.UserData;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws DataAccessException {
        String createTable = """
                CREATE TABLE IF NOT EXISTS user (
                `username` VARCHAR(256) NOT NULL,
                `password` VARCHAR(256) NOT NULL,
                `email` VARCHAR(256) NOT NULL,
                PRIMARY KEY (`username`)
                )
                """;
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(createTable)){
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }
}
