package dao.mappers;

import dao.model.Mission;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MissionRowMapper {
    public Mission mapRow(ResultSet rs) throws SQLException {
        Mission mission = new Mission();
        mission.setMissionId(rs.getInt("mission_id"));
        mission.setName(rs.getString("name"));
        mission.setDescription(rs.getString("description"));
        mission.setTokens(rs.getInt("tokens"));
        return mission;
    }
}
