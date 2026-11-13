package client;

import com.google.gson.Gson;
import model.AuthData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public ServerFacade(String url){
        this.serverUrl = url;
    }

    //1. clear the database
    public void clear() throws Exception{
        URI uri = new URI(serverUrl + "/db");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode()!= 200){
            throw new Exception("Error clearing database: " + response.statusCode());
        }
    }

    //2. register a new user
    public AuthData register(String username, String password, String email) throws Exception{
        URI uri = new URI(serverUrl + "/user");
        Map<String, String> body = Map.of("username", username, "password", password, "email", email);
        String jsonBody = gson.toJson(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200){
            return gson.fromJson(response.body(), AuthData.class);
        }else{
            throw new Exception("Registration failed: " + response.statusCode() + " - " + response.body());
        }
    }
}
