package client;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:"+ port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDatabase() throws Exception{
        facade.clear();
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    void loginSuccess() throws Exception{
        facade.register("player1", "password", "p1@wmail.com");
        AuthData authData = facade.login("player1", "password");
        Assertions.assertNotNull(authData.authToken());
    }

    @Test
    void loginFailureBadPassword() throws Exception{
        facade.register("player1", "password", "p1@wmail.com");
        Assertions.assertThrows(Exception.class, () -> facade.login("player1", "wrongpassword"));
    }

    @Test
    void logoutSuccess() throws Exception{
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        facade.logout(authData.authToken());
        Assertions.assertThrows(Exception.class, () -> facade.createGame(authData.authToken(), "testGame"));
    }

    @Test
    void logoutFailureInvalidToken(){
        Assertions.assertThrows(Exception.class, () -> facade.logout("fake-token"));
    }

    @Test
    void createGameSuccess() throws Exception{
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        int gameId = facade.createGame(authData.authToken(), "My New Game");
        Assertions.assertTrue(gameId > 0);
    }

    @Test
    void createGameFailureBadAuth(){
        Assertions.assertThrows(Exception.class, () -> facade.createGame("fake-token", "My New Game"));
    }

    @Test
    void listGamesSuccess() throws Exception{
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        facade.createGame(authData.authToken(), "Game 1");
        facade.createGame(authData.authToken(), "Game 2");

        var games = facade.listGames(authData.authToken());
        Assertions.assertEquals(2, games.length);

    }

    @Test
    void listGamesFailureBadAuth(){
        Assertions.assertThrows(Exception.class, () ->facade.listGames("fake-token"));
    }

    @Test
    void joinGameSuccess() throws Exception{
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        int gameId = facade.createGame(authData.authToken(), "Game 1");
        Assertions.assertDoesNotThrow(() -> facade.joinGame(authData.authToken(), "WHITE", gameId));

    }

    @Test
    void joinGameFailureBadGameID() throws Exception{
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertThrows(Exception.class, () -> facade.joinGame(authData.authToken(), "WHITE", 9999));
    }

    @Test
    void registerSuccess() throws Exception{
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertNotNull(authData.authToken());
        Assertions.assertEquals("player1", authData.username());
    }

    @Test
    void registerFailureDuplicateUser() throws Exception{
        facade.register("player1", "password", "p1@email.com");
        Assertions.assertThrows(Exception.class, () -> facade.register("player1", "password", "p1@email.com"));
    }


}