package dao.interfaces;

import dao.model.Player;
import dao.model.PlayerCharacter;

import java.util.List;

public interface PlayerRepository {
    int savePlayerWithCharacters(Player player, List<PlayerCharacter> characters);
    void updatePlayer(Player player);
    Player getByName(String name);
    Player getById(int id);
    void updateTokens(int playerId, int newTokens);
}
