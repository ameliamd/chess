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
}
