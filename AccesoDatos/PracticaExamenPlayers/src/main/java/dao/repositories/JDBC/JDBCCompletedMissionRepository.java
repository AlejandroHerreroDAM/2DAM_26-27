package dao.repositories.JDBC;

import common.Constants;
import dao.interfaces.CompletedMissionRepository;
import dao.mappers.CompletedMissionsRowMapper;
import dao.model.CompletedMission;
import dao.repositories.common.SQLQueries;
import domain.exceptions.DatabaseError;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCCompletedMissionRepository implements CompletedMissionRepository {

    private final DBConnectionPool db;
    private final CompletedMissionsRowMapper completedMissionsRowMapper;

    @Inject
    public JDBCCompletedMissionRepository(DBConnectionPool db, CompletedMissionsRowMapper completedMissionsRowMapper) {
        this.db = db;
        this.completedMissionsRowMapper = completedMissionsRowMapper;
    }


    @Override
    public List<CompletedMission> getAllByPlayerId(int playerId) {
        List<CompletedMission> missions = new ArrayList<>();
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(SQLQueries.GET_COMPLETED_MISSIONS_BY_PLAYER_ID)) {

            ps.setInt(1, playerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    missions.add(completedMissionsRowMapper.mapRow(rs));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(JDBCCompletedMissionRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
        return missions;
    }

    @Override
    public List<CompletedMission> getAllByCharacterName(String characterName) {
        List<CompletedMission> missions = new ArrayList<>();
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(SQLQueries.GET_COMPLETED_MISSIONS_BY_CHARACTER_NAME)) {

            ps.setString(1, characterName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    missions.add(completedMissionsRowMapper.mapRow(rs));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(JDBCCompletedMissionRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
        return missions;
    }


    @Override
    public void saveCompletedMission(CompletedMission mission) {
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(SQLQueries.INSERT_COMPLETED_MISSION)) {

            ps.setInt(1, mission.getMissionId());
            ps.setInt(2, mission.getCharacterId());
            ps.setDate(3, Date.valueOf(mission.getCompletionDate()));
            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException ex) {
            throw new DatabaseError(Constants.DATABASE_ERROR);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCCompletedMissionRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }

    @Override
    public void deleteAllByCharacterName(String characterName) {

    }
}
