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
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement("TRUNCATE TABLE user")){
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()){
                    if (rs.next()){
                        return new UserData(
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(user.password(), org.mindrot.jbcrypt.BCrypt.gensalt());

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)){

            preparedStatement.setString(1, user.username());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, user.email());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }
}
