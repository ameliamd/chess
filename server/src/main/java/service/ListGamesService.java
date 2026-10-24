package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import model.GameData;
import java.util.Collection;

public class ListGamesService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException{
        if (authDAO.getAuth(authToken) == null){
            throw new DataAccessException("unauthorized");
        }
        return gameDAO.listGames();
    }
}
