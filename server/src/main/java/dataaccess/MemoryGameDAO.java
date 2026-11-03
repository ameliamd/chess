package dataaccess;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear(){
        games.clear();
    }

    @Override
    public Collection<GameData> listGames(){
        return games.values();
    }

    @Override
    public int createGame(GameData game){
        games.put(game.gameID(), game);
        return game.gameID();
    }

    @Override
    public GameData getGame(int gameID){
        return games.get(gameID);
    }

    @Override
    public void updateGame(GameData game){
        games.put(game.gameID(), game);
    }
}
