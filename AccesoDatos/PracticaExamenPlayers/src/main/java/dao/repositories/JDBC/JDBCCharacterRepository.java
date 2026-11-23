package dao.repositories.JDBC;

import common.Constants;
import dao.interfaces.PlayerCharacterRepository;
import dao.mappers.CharacterRowMapper;
import dao.model.PlayerCharacter;
import dao.repositories.common.SQLQueries;
import domain.exceptions.DatabaseError;
import domain.exceptions.DuplicatedUsernameError;
import domain.exceptions.ForeignKeyError;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCCharacterRepository implements PlayerCharacterRepository {

    private final DBConnectionPool db;
    private final CharacterRowMapper characterRowMapper;

    @Inject
    public JDBCCharacterRepository(DBConnectionPool db, CharacterRowMapper characterRowMapper) {
        this.db = db;
        this.characterRowMapper = characterRowMapper;
    }

    @Override
    public int saveCharacter(PlayerCharacter character) {
        int id = 0;

        try (Connection con = db.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(SQLQueries.INSERT_CHARACTER, Statement.RETURN_GENERATED_KEYS)) {

                con.setAutoCommit(false);

                ps.setInt(1, character.getPlayerId());
                ps.setString(2, character.getName());
                ps.setString(3, character.getType());
                ps.setInt(4, character.getLevel());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    id = rs.getInt(1);
                    character.setCharacterId(id);
                }

                con.commit();
            } catch (SQLIntegrityConstraintViolationException ex) {
                con.rollback();
                throw new DuplicatedUsernameError(Constants.DUPLICATED_USERNAME_ERROR);
            } catch (SQLException e) {
                Logger.getLogger(JDBCCharacterRepository.class.getName()).log(Level.SEVERE, null, e);
                con.rollback();
                throw new DatabaseError(Constants.DATABASE_ERROR);
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JDBCCharacterRepository.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
        return id;
    }

    @Override
    public void deleteCharacter(int id, boolean confirmation) {
        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);

            try {
                if (confirmation) {
                    try (PreparedStatement statementCMissions = con.prepareStatement(SQLQueries.DELETE_COMPLETED_MISSIONS_ON)) {
                        statementCMissions.setInt(1, id);
                        statementCMissions.executeUpdate();
                    }
                }

                try (PreparedStatement statementJugdor = con.prepareStatement(SQLQueries.DELETE_CHARACTER_BY_ID)) {
                    statementJugdor.setInt(1, id);

                    if (statementJugdor.executeUpdate() == 0) {
                        throw new DatabaseError(Constants.NO_PLAYER_ERROR + id);
                    }
                }
                con.commit();

            } catch (SQLIntegrityConstraintViolationException ex) {
                con.rollback();
                throw new ForeignKeyError(Constants.DATABASE_ERROR);
            } catch (SQLException e) {
                Logger.getLogger(JDBCCharacterRepository.class.getName()).log(Level.SEVERE, null, e);
                con.rollback();
                throw new DatabaseError(Constants.DATABASE_ERROR);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCCharacterRepository.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }


    @Override
    public PlayerCharacter getByName(String name) {
        PlayerCharacter character = null;
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(SQLQueries.SELECT_CHARACTER_BY_NAME)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    character = characterRowMapper.mapRow(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCCharacterRepository.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
        return character;
    }
}
