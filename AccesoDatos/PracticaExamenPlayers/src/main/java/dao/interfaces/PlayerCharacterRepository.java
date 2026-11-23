package dao.interfaces;

import dao.model.PlayerCharacter;

public interface PlayerCharacterRepository {
    int saveCharacter(PlayerCharacter character);
    void deleteCharacter(int id, boolean confirmation);
    PlayerCharacter getByName(String name);
}
