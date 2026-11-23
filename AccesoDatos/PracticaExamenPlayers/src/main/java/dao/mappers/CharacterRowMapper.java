package dao.mappers;

import dao.model.PlayerCharacter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacterRowMapper {
    public PlayerCharacter mapRow(ResultSet rs) throws SQLException {
        PlayerCharacter character = new PlayerCharacter();
        character.setCharacterId(rs.getInt("character_id"));
        character.setName(rs.getString("name"));
        character.setType(rs.getString("type"));
        character.setLevel(rs.getInt("level"));
        character.setPlayerId(rs.getInt("player_id"));
        return character;
    }
}
