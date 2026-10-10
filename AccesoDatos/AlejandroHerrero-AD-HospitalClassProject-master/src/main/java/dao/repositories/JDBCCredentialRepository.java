package dao.repositories;

import common.Constants;
import dao.interfaces.CredentialRepository;
import dao.mapper.credentialQueryMapper;
import dao.model.UserLogin;
import dao.repositories.common.SQLQueries;
import domain.exceptions.DatabaseError;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCCredentialRepository implements CredentialRepository {
    private final DBConnection db;
    private final credentialQueryMapper credentialMapper;

    @Inject
    public JDBCCredentialRepository(DBConnection db, credentialQueryMapper credentialMapper) {
        this.db = db;
        this.credentialMapper = credentialMapper;
    }

    @Override
    public UserLogin get(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }

        try (Connection con = db.getConnection();
             PreparedStatement statement = con.prepareStatement(SQLQueries.SELECT_CREDENTIAL_BY_USERNAME)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return credentialMapper.mapCredential(rs);
            }
        } catch (SQLException ex) {
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }
}
