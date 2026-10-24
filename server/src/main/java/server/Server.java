package server;

import dataaccess.GameDAO;
import dataaccess.UserDAO;
import handler.*;
import io.javalin.*;
import service.*;
import dataaccess.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.exception(DataAccessException.class, (ex, ctx) ->{
            ctx.status(500);
            if(ex.getMessage().contains("already taken")){
                ctx.status(403);
            } else if(ex.getMessage().contains("bad request")) {
                ctx.status(400);
            } else if(ex.getMessage().contains("unauthorized")) {
                ctx.status(401);
            }
            var serializer = new com.google.gson.Gson();
            ctx.result(serializer.toJson(java.util.Map.of("message", "Error: " + ex.getMessage())));
                });

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);

        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
        ClearHandler clearHandler = new ClearHandler(clearService);

        javalin.delete("/db", clearHandler);

        RegisterService registerService = new RegisterService(userDAO, authDAO);
        RegisterHandler registerHandler = new RegisterHandler(registerService);
        javalin.post("/user", registerHandler);


        LoginService loginService = new LoginService(userDAO, authDAO);
        LoginHandler loginHandler = new LoginHandler(loginService);

        javalin.post("/session", loginHandler);

        LogoutService logoutService = new LogoutService(authDAO);
        LogoutHandler logoutHandler = new LogoutHandler(logoutService);
        javalin.delete("/session", logoutHandler);

        ListGamesService listGamesService = new ListGamesService(authDAO, gameDAO);
        ListGamesHandler listGamesHandler = new ListGamesHandler(listGamesService);
        javalin.get("/game", listGamesHandler);

        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        CreateGameHandler createGameHandler = new CreateGameHandler(createGameService);
        javalin.post("/game", createGameHandler);

        JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO);
        JoinGameHandler joinGameHandler = new JoinGameHandler(joinGameService);
        javalin.put("/game", joinGameHandler);

        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
