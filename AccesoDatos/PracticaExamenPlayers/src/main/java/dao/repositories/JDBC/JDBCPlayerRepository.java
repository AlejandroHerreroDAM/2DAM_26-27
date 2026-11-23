package dao.repositories.JDBC;

import common.Constants;
import dao.interfaces.PlayerRepository;
import dao.mappers.PlayerRowMapper;
import dao.model.Player;
import dao.model.PlayerCharacter;
import dao.repositories.common.SQLQueries;
import domain.exceptions.DatabaseError;
import domain.exceptions.DuplicatedUsernameError;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCPlayerRepository implements PlayerRepository {

    private final DBConnectionPool db;
    private final PlayerRowMapper playerRowMapper;

    @Inject
    public JDBCPlayerRepository(DBConnectionPool db, PlayerRowMapper playerRowMapper) {
        this.db = db;
        this.playerRowMapper = playerRowMapper;
    }
    
    @Override
    public int savePlayerWithCharacters(Player player, List<PlayerCharacter> characters) {
        int id = 0;
        
        try (Connection con = db.getConnection()) {
            try (PreparedStatement psPlayer =  con.prepareStatement(SQLQueries.INSERT_PLAYER_WITH_AUTO_ID, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psCharacter = con.prepareStatement(SQLQueries.INSERT_CHARACTER)) {
                
                con.setAutoCommit(false);
                
                psPlayer.setString(1, player.getName());
                psPlayer.setString(2, player.getEmail());
                psPlayer.setInt(3, player.getTokens());
                psPlayer.executeUpdate();

                try (ResultSet rs = psPlayer.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                        player.setPlayerId(id);
                    }
                }

                if (characters != null) {
                    for (PlayerCharacter character : characters) {
                        psCharacter.setInt(1, id);
                        psCharacter.setString(2, character.getName());
                        psCharacter.setString(3, character.getType());
                        psCharacter.setInt(4, character.getLevel());
                        psCharacter.executeUpdate();
                    }
                }

                con.commit();

            } catch (SQLIntegrityConstraintViolationException ex) {
                con.rollback();
                throw new DuplicatedUsernameError(Constants.DUPLICATED_USERNAME_ERROR);
            } catch (SQLException e) {
                Logger.getLogger(JDBCPlayerRepository.class.getName()).log(Level.SEVERE, null, e);
                con.rollback();
                throw new DatabaseError(Constants.DATABASE_ERROR);
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException ex) {
            Logger.getLogger(JDBCPlayerRepository.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
        return id;
    }

    @Override
    public void updatePlayer(Player player) {
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(SQLQueries.UPDATE_PLAYER)) {
            ps.setString(1, player.getName());
            ps.setString(2, player.getEmail());
            ps.setInt(3, player.getTokens());
            ps.setInt(4, player.getPlayerId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }

    @Override
    public Player getByName(String name) {
        Player player = null;
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(SQLQueries.SELECT_PLAYER_BY_NAME)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    player = playerRowMapper.mapRow(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPlayerRepository.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
        return player;
    }

    @Override
    public Player getById(int id) {
        Player player = null;
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(SQLQueries.SELECT_PLAYER_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    player = playerRowMapper.mapRow(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPlayerRepository.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
        return player;
    }

    @Override
    public void updateTokens(int playerId, int newTokens) {
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(SQLQueries.UPDATE_PLAYER_TOKENS)) {
            ps.setInt(1, newTokens);
            ps.setInt(2, playerId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPlayerRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }

}
