package client;

import model.AuthData;
import model.GameData;

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
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Thanks for playing!!!!");

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
                System.out.println("Joining game...");
                break;
            case "observe":
                System.out.println("Observing game...");
                break;
            case "logout":
                facade.logout(authToken);
                this.authToken = null;
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
