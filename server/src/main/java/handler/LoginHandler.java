package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.AuthData;
import model.UserData;
import service.LoginService;

public class LoginHandler implements Handler {
    private final LoginService service;

    public LoginHandler(LoginService service) {
        this.service = service;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        var serializer = new Gson();

        UserData requestData = serializer.fromJson(ctx.body(), UserData.class);
        AuthData result = service.login(requestData);
        ctx.status(200);
        ctx.result(serializer.toJson(result));
    }
}

