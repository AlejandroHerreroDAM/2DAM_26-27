package dao.mappers;

import dao.model.CompletedMission;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompletedMissionsRowMapper {
    public CompletedMission mapRow(ResultSet rs) throws SQLException {
        CompletedMission completedMission = new CompletedMission();
        completedMission.setMissionId(rs.getInt("mission_id"));
        completedMission.setCharacterId(rs.getInt("character_id"));
        completedMission.setCompletionDate(rs.getDate("completion_date").toLocalDate());
        completedMission.setCharacterName(rs.getString("character_name"));
        return completedMission;
    }
}
