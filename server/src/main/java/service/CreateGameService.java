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
        if(request == null || request.gameName() == null || request.gameName().isEmpty()){
            throw new DataAccessException("Error: bad request");
        }

        if(authDAO.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }

        GameData newGame = new GameData(0, null, null, request.gameName(), new ChessGame());

        int actualGameID = gameDAO.createGame(newGame);

        return actualGameID;

    }

}
