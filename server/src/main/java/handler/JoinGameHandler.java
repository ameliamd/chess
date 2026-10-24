package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.JoinGameRequest;
import service.JoinGameService;

public class JoinGameHandler implements Handler{
    private final JoinGameService service;

    public JoinGameHandler(JoinGameService service){
        this.service = service;
    }
    @Override
    public void handle(Context ctx) throws Exception{
        String authToken = ctx.header("authorization");

        var serializer = new Gson();
        JoinGameRequest requestData = serializer.fromJson(ctx.body(), JoinGameRequest.class);

        service.joinGame(authToken, requestData);

        ctx.status(200);
        ctx.result("{}");
    }
}
