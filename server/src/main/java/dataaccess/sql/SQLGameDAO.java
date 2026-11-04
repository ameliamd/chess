package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        String createTable = """
                CREATE TABLE IF NOT EXISTS chess_game (
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
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement("TRUNCATE TABLE chess_game")){
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new java.util.ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM chess_game";
            try (var preparedStatement  = conn.prepareStatement(statement)){
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var jsonString = rs.getString("game");
                        var chessGame = new com.google.gson.Gson().fromJson(jsonString, chess.ChessGame.class);

                        result.add(new GameData(
                                rs.getInt("gameID"),
                                rs.getString("whiteUsername"),
                                rs.getString("blackUsername"),
                                rs.getString("gameName"),
                                chessGame
                        ));
                    }
                }
            }
            } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return result;
    }

    @Override
    public int createGame(GameData game) throws DataAccessException {
        String statement = "INSERT INTO chess_game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){

            preparedStatement.setString(1, game.whiteUsername());
            preparedStatement.setString(2, game.blackUsername());
            preparedStatement.setString(3, game.gameName());

            var jsonGame = new com.google.gson.Gson().toJson(game.game());
            preparedStatement.setString(4, jsonGame);

            preparedStatement.executeUpdate();

            try (var rs = preparedStatement.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        return 0;

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM chess_game WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()){
                    if (rs.next()){
                        var jsonString = rs.getString("game");
                        var chessGame = new com.google.gson.Gson().fromJson(jsonString, chess.ChessGame.class);
                        return new GameData(
                                rs.getInt("gameID"),
                                rs.getString("whiteUsername"),
                                rs.getString("blackUsername"),
                                rs.getString("gameName"),
                                chessGame

                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        String statement = "UPDATE chess_game SET whiteUsername = ?, blackUsername = ?, gameName=?, game=? WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, game.whiteUsername());
            preparedStatement.setString(2, game.blackUsername());
            preparedStatement.setString(3, game.gameName());

            var jsonGame = new com.google.gson.Gson().toJson(game.game());
            preparedStatement.setString(4, jsonGame);
            preparedStatement.setInt(5, game.gameID());
            if(preparedStatement.executeUpdate() == 0){
                throw new DataAccessException("Error: Game does not exist.");
            }
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }
}
