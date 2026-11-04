package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.JoinGameRequest;

public class JoinGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void joinGame(String authToken, JoinGameRequest request) throws DataAccessException{
        if(request == null){
            throw new DataAccessException("Error: bad request");
        }

        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null){
            throw new DataAccessException("Error: unauthorized");
        }

        GameData game = gameDAO.getGame(request.gameID());
        if(game == null){
            throw new DataAccessException("Error: bad request");
        }

        String color = request.playerColor();
        if (color == null || (!color.equals("WHITE") && !color.equals("BLACK"))){
            throw new DataAccessException("Error: bad request");

        }

        String username = auth.username();
        GameData updatedGame;

        if(color.equals("WHITE")){
            if(game.whiteUsername() != null){
                throw new DataAccessException("Error: already taken");
            }
            updatedGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        } else {
            if(game.blackUsername() != null){
                throw new DataAccessException("Error: already taken");
            }
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }
        gameDAO.updateGame(updatedGame);
    }

}
