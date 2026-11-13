package client;

import model.AuthData;
import model.GameData;
import ui.DrawBoard;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Repl {
    public enum State{
        SIGNEDOUT,
        SIGNEDIN,
        QUIT
    }

    private State state = State.SIGNEDOUT;
    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade facade;
    private String authToken = null;
    private GameData[] gamesList = null;

    public Repl(String serverUrl){
        this.facade = new ServerFacade(serverUrl);
    }

    public void run(){
        System.out.println("Welcome to Chess. Type Help to get started.");
        while (!state.equals(State.QUIT)){
            if(state == State.SIGNEDOUT){
                System.out.print("[LOGGED OUT] >>> ");
            } else {
                System.out.print("[LOGGED IN] >>> ");
            }

            String input = scanner.nextLine();

            try{
                if(state == State.SIGNEDOUT){
                    handlePreLogin(input);
                } else {
                    handlePostLogin(input);
                }
            } catch (Exception e) {
                System.out.println(formatErrorMessage(e));
            }
        }
        System.out.println("Thanks for playing!!!!");

    }

    private String formatErrorMessage(Exception e){
        String raw = (e.getMessage() == null) ? "" : e.getMessage();
        String lower = raw.toLowerCase();
        if (lower.contains("400") || lower.contains("bad request")){
            return "Invalid request. Please check your input and try again.";
        }
        if (lower.contains("401") || lower.contains("unauthorized")){
            return "Invalid username or password.";
        }

        if (lower.contains("403") || lower.contains("already taken")){
            return "That username or game spot is already taken.";
        }

        if (lower.contains("404") || lower.contains("not found")){
            return "Requested resource was not found.";
        }
        if (lower.contains("500") || lower.contains("internal server error")){
            return "Server error. Please try again later.";
        }

        if (raw.contains("{") || raw.contains("}")){
            return "Request failed. Please try again.";
        }

        if (raw.isBlank()){
            return "Something went wrong. Please try again.";
        }
        return raw;


    }


    private void handlePreLogin(String input) throws Exception{
        var tokens = input.trim().toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);

        switch(cmd) {
            case "register":
                if(params.length == 3){
                    AuthData auth = facade.register(params[0], params[1], params[2]);
                    this.authToken = auth.authToken();
                    System.out.println("Registered and logged in successfully!");
                    state = State.SIGNEDIN;
                } else{
                    System.out.println("Expected: <USERNAME> <PASSWORD> <EMAIL>");
                }
                break;

            case "login":
                if(params.length == 2){
                    AuthData auth = facade.login(params[0], params[1]);
                    this.authToken = auth.authToken();
                    System.out.println("Logged in successfully!");
                    state = State.SIGNEDIN;
                } else{
                    System.out.println("Expected: <USERNAME> <PASSWORD>");
                }
                break;

            case "quit":
                state = State.QUIT;
                break;
            case "help":
            default:
                System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
                System.out.println("login <USERNAME> <PASSWORD> - to play chess");
                System.out.println("quit - playing chess");
                System.out.println("help - with possible commands");
                break;

        }

    }

    private void handlePostLogin(String input) throws Exception{
        var tokens = input.trim().toLowerCase().split(" ");
        var cmd =  (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);

        switch (cmd) {
            case "create":
                if(params.length == 1){
                    int gameID = facade.createGame(authToken, params[0]);
                    System.out.println("Created game '" + params[0] + "' with ID: " + gameID);
                }else{
                    System.out.println("Expected: <NAME>");
                }
                break;
            case "list":
                gamesList = facade.listGames(authToken);
                if(gamesList.length == 0){
                    System.out.println("No active games.");
                }else {
                    System.out.println("Active Games:");
                    for (int i = 0; i < gamesList.length; i++) {
                        GameData game = gamesList[i];
                        System.out.printf("%d. %s (White: %s, Black: %s) \n",
                                i + 1,
                                game.gameName(),
                                game.whiteUsername() == null ? "None" : game.whiteUsername(),
                                game.blackUsername() == null ? "None" : game.blackUsername());
                    }
                }
                break;
            case "play":
                if(params.length == 2){
                    try{
                        if(gamesList == null){
                            gamesList = facade.listGames(authToken);
                        }
                        int index = Integer.parseInt(params[0]) - 1;
                        int gameID = gamesList[index].gameID();
                        String color = params[1].toUpperCase();

                        facade.joinGame(authToken, color, gameID);
                        System.out.println("Joined game successfully!!");
                        DrawBoard.draw(color);
                    } catch (Exception e) {
                        System.out.println("Error joining game. Did you type a valid number and color?");
                    }
                }else{
                    System.out.println("Expected: <ID> [WHITE|BLACK]");
                }
                break;
            case "observe":
                if(params.length == 1){
                    try{
                        if(gamesList == null){
                            gamesList = facade.listGames(authToken);
                        }
                        int index = Integer.parseInt(params[0]) -1;
                        if(gamesList == null || index < 0 || index >= gamesList.length){
                            System.out.println("Invalid game number. Run 'List' to see available games.");
                            break;
                        }
                        System.out.println("Observing game...");
                        DrawBoard.draw("WHITE");
                    } catch (NumberFormatException e){
                        System.out.println("Expected a valid game number: <ID>");
                    }
                    }else {
                    System.out.println("Expected: <ID>");
                }
                break;
            case "logout":
                facade.logout(authToken);
                this.authToken = null;
                this.gamesList = null;
                System.out.println("Logged out successfully!");
                state = State.SIGNEDOUT;
                break;
            case "help":
            default:
                System.out.println("create <name> - make a new game");
                System.out.println("list - see all active games");
                System.out.println("play <ID> [WHITE|BLACK] - join a game");
                System.out.println("observe <ID> - watch a game");
                System.out.println("logout - sign out");
                System.out.println("help - with possible commands");
                break;

        }
    }
}
