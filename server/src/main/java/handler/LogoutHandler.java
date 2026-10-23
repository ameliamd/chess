package handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.LogoutService;

public class LogoutHandler implements Handler {
    private final LogoutService service;

    public LogoutHandler(LogoutService service){
        this.service = service;
    }

    @Override
    public void handle(Context ctx) throws Exception{
        String authToken = ctx.header("authorization");
        service.logout(authToken);
        ctx.status(200);
        ctx.result("{}");
    }
}
