package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import model.GameData;
import model.CreateGameRequest;
import chess.ChessGame;
import java.util.Random;

public class CreateGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public int createGame(String authToken, CreateGameRequest request) throws DataAccessException{
        if(authDAO.getAuth(authToken) == null){
            throw new DataAccessException("unauthorized");
        }
        if(request.gameName() == null || request.gameName().isEmpty()){
            throw new DataAccessException("bad request");
        }
        int newGameID = Math.abs(new Random().nextInt());
        GameData newGame = new GameData(newGameID, null, null, request.gameName(), new ChessGame());

        gameDAO.createGame(newGame);

        return newGameID;
    }

}
