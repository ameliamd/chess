package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ListGamesService;
import java.util.Map;

public class ListGamesHandler implements Handler{
    private final ListGamesService service;

    public ListGamesHandler(ListGamesService service){
        this.service = service;
    }

    @Override
    public void handle(Context ctx) throws Exception{
        String authToken = ctx.header("authorization");

        var games = service.listGames(authToken);

        var serializer = new Gson();
        ctx.status(200);
        ctx.result(serializer.toJson(Map.of("games", games)));
    }
}
