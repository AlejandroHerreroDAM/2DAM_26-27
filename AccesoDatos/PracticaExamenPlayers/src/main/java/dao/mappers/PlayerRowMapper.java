package dao.mappers;

import dao.model.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRowMapper {
    public Player mapRow(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.setPlayerId(rs.getInt("player_id"));
        player.setName(rs.getString("name"));
        player.setEmail(rs.getString("email"));
        player.setTokens(rs.getInt("tokens"));
        return player;
    }
}
