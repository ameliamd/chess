package client;

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

    public Repl(){
    }

    public void run(){
        System.out.println("Welcome to Chess. Type Help to get started.");
        while (!state.equals(State.QUIT)){
            if(state == State.SIGNEDOUT){
                System.out.print("LOGGED OUT");
            } else {
                System.out.print("LOGGED IN");
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

        if(state == State.SIGNEDOUT){
            System.out.print("LOGGED OUT");
        } else {
            System.out.print("LOGGED IN");
        }
    }


    private void handlePreLogin(String input){
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);

        switch(cmd) {
            case "register":
                System.out.println("Registered successfully! (Not really yet");
                state = State.SIGNEDIN;
                break;

            case "login":
                System.out.println("Logged in successfully! (Not really yet)");
                state = State.SIGNEDIN;
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

    private void handlePostLogin(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd =  (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);

        switch (cmd) {
            case "create":
                System.out.println("Creating game...");
                break;
            case "list":
                System.out.println("Listing games...");
                break;
            case "play":
                System.out.println("Joining game...");
                break;
            case "observe":
                System.out.println("Observing game...");
                break;
            case "logout":
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
