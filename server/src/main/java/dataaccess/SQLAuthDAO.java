package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() throws DataAccessException {
        String createTable = """
                CREATE TABLE IF NOT EXISTS auth (
                `authToken` VARCHAR(256) NOT NULL,
                `username` VARCHAR(256) NOT NULL,
                PRIMARY KEY (`authToken`)
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
    public void createAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
