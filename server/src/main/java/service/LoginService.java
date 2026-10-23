package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.LoginRequest;
import java.util.UUID;

public class LoginService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData login(LoginRequest request) throws DataAccessException{
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, request.username());

        return authData;

    }
}
