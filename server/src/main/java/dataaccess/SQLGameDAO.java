package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() throws DataAccessException {
        String createTable = """
                CREATE TABLE IF NOT EXISTS game (
                `gameID` INT NOT NULL AUTO_INCREMENT,
                `whiteUsername` VARCHAR(256),
                `blackUsername` VARCHAR(256),
                `gameName` VARCHAR(256) NOT NULL,
                `game` TEXT NOT NULL,
                PRIMARY KEY (`gameID`)
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
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }
}
