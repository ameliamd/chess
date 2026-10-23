package handler;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import service.RegisterService;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class RegisterHandler implements Handler {
    private final RegisterService service;

    public RegisterHandler(RegisterService service){
        this.service = service;
    }

    @Override
    public void handle(Context ctx) throws Exception{
        var serializer = new Gson();

        UserData requestData = serializer.fromJson(ctx.body(), UserData.class);
        AuthData result = service.register(requestData);
        ctx.status(200);
        ctx.result(serializer.toJson(result));
        }
    }
}
