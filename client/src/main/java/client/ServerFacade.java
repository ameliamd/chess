package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

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

    public AuthData login(String username, String password) throws Exception {
        URI uri = new URI(serverUrl + "/session");
        Map<String, String> body = Map.of("username", username, "password", password);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), AuthData.class);
        } else {
            throw new Exception("Login failed: " + response.statusCode());
        }
    }

        public void logout(String authToken) throws Exception{
            URI uri = new URI(serverUrl + "/session");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .DELETE()
                    .header("authorization", authToken)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200){
                throw new Exception("Logout failed: " + response.statusCode());
            }
        }

        public int createGame(String authToken, String gameName) throws Exception{
            URI uri = new URI(serverUrl + "/game");
            Map<String, String> body = Map.of("gameName", gameName);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                    .header("authorization", authToken)
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200){
                Map responseMap = gson.fromJson(response.body(), Map.class);
                return ((Double) responseMap.get("gameID")).intValue();
            }else{
                throw new Exception("Create game failed: " + response.statusCode());
            }
        }

        private record ListGamesResponse(GameData[] games) {}

        public GameData[] listGames(String authToken) throws Exception{
            URI uri = new URI(serverUrl + "/game");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .header("authorization", authToken)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200){
                ListGamesResponse listResponse = gson.fromJson(response.body(), ListGamesResponse.class);
                return listResponse.games();
            }else{
                throw new Exception("List games failed: " + response.statusCode());
            }
        }

        public void joinGame(String authToken, String playerColor, int gameID) throws Exception{
            URI uri = new URI(serverUrl + "/game");
            Map<String, Object> body = Map.of("playerColor", playerColor, "gameID", gameID);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                    .header("authorization", authToken)
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200){
                throw new Exception("Join game failed: " + response.statusCode());
            }
        }

    }

