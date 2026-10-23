package handler;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ClearService;

public class ClearHandler implements Handler{
    private final ClearService service;

    public ClearHandler(ClearService service){
        this.service = service;
    }

    @Override
    public void handle(Context ctx) throws Exception{
        service.clearAll();
        ctx.status(200);
        ctx.result("{}");
    }
}
