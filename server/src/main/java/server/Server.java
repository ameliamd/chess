package server;

import dataaccess.GameDAO;
import dataaccess.UserDAO;
import handler.ClearHandler;
import handler.ListGamesHandler;
import handler.LoginHandler;
import handler.LogoutHandler;
import io.javalin.*;
import service.ClearService;
import dataaccess.*;
import service.ListGamesService;
import service.LoginService;
import service.LogoutService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);

        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
        ClearHandler clearHandler = new ClearHandler(clearService);

        javalin.delete("/db", clearHandler);


        LoginService loginService = new LoginService(userDAO, authDAO);
        LoginHandler loginHandler = new LoginHandler(loginService);

        javalin.post("/session", loginHandler);

        LogoutService logoutService = new LogoutService(authDAO);
        LogoutHandler logoutHandler = new LogoutHandler(logoutService);
        javalin.delete("/session", loginHandler);

        ListGamesService listGamesService = new ListGamesService(authDAO, gameDAO);
        ListGamesHandler listGamesHandler = new ListGamesHandler(listGamesService);
        javalin.get("/game", listGamesHandler);

        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
