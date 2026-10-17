package org.hospitalspringclient.dao.repositories;

import org.hospitalspringclient.common.Constants;
import org.hospitalspringclient.dao.interfaces.CredentialRepository;
import org.hospitalspringclient.dao.mapper.credentialQueryMapper;
import org.hospitalspringclient.dao.model.UserLogin;
import org.hospitalspringclient.dao.repositories.common.SQLQueries;
import org.hospitalspringclient.domain.exceptions.DatabaseError;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JDBCCredentialRepository implements CredentialRepository {
    private final DBConnection db;
    private final credentialQueryMapper credentialMapper;

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
             PreparedStatement statement = con.prepareStatement(SQLQueries.GET_USER_BY_USERNAME)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return credentialMapper.mapCredential(rs);
            }
        } catch (SQLException ex) {
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }
}
