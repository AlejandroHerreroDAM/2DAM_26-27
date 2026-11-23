package dao.repositories.JDBC;

import common.Constants;
import dao.interfaces.MissionRepository;
import dao.mappers.MissionRowMapper;
import dao.model.Mission;
import dao.repositories.common.SQLQueries;
import domain.exceptions.DatabaseError;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCMissionRepository implements MissionRepository {

    private final DBConnectionPool db;
    private final MissionRowMapper missionRowMapper;

    @Inject
    public JDBCMissionRepository(DBConnectionPool db, MissionRowMapper missionRowMapper) {
        this.db = db;
        this.missionRowMapper = missionRowMapper;
    }

    @Override
    public Mission getByName(String name) {
        Mission mission = null;
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(SQLQueries.SELECT_MISSION_BY_NAME)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mission = missionRowMapper.mapRow(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPlayerRepository.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
        return mission;
    }
}
