package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO =  authDAO;
    }

    public AuthData register(UserData user) throws DataAccessException{
        if(user.username() == null || user.password() == null || user.email() == null){
            throw new DataAccessException("bad request");
        }

        userDAO.createUser(user);

        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, user.username());
        authDAO.createAuth(authData);

        return authData;

    }
}
