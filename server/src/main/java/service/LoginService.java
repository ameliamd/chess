package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class LoginService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData login(UserData request) throws DataAccessException{
        if(request.username() == null || request.password() == null){
            throw new DataAccessException("bad request");
        }

        UserData user = userDAO.getUser(request.username());

        if(user == null || !user.password().equals(request.password())){
            throw new DataAccessException("unauthorized");
        }

        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, user.username());
        authDAO.createAuth(authData);

        return authData;

    }
}
