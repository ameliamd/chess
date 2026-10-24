package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.CreateGameRequest;
import service.CreateGameService;
import java.util.Map;

public class CreateGameHandler implements Handler{
    private final CreateGameService service;

    public CreateGameHandler(CreateGameService service){
        this.service = service;
    }

    @Override
    public void handle(Context ctx) throws Exception{
        String authToken = ctx.header("authorization");

        var serializer = new Gson();
        CreateGameRequest requestData = serializer.fromJson(ctx.body(), CreateGameRequest.class);

        int gameID = service.createGame(authToken, requestData);

        ctx.status(200);
        ctx.result(serializer.toJson(Map.of("gameID", gameID)));
    }
}
