package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO){
        this.authDAO = authDAO;
    }

    public void logout(String authToken) throws DataAccessException{
        if (authDAO.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }

        authDAO.deleteAuth(authToken);
    }
}
