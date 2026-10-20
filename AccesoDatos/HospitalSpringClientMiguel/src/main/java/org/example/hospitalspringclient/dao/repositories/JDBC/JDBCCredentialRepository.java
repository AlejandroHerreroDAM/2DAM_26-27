package org.example.hospitalspringclient.dao.repositories.JDBC;



import org.example.hospitalspringclient.common.Constants;
import org.example.hospitalspringclient.dao.mappers.CredentialRowMapper;
import org.example.hospitalspringclient.dao.model.Credential;
import org.example.hospitalspringclient.dao.repositories.CredentialRepository;
import org.example.hospitalspringclient.dao.repositories.common.SQLQueries;
import org.example.hospitalspringclient.domain.errors.DatabaseError;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class JDBCCredentialRepository implements CredentialRepository {
    private final DBConnection db;
    private final CredentialRowMapper crm;

    public JDBCCredentialRepository(DBConnection db, CredentialRowMapper crm) {
        this.db = db;
        this.crm = crm;
    }

    @Override
    public Credential getByUsername(String username) {
        Credential credential = null;
        try {
            Connection c = db.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement(SQLQueries.SELECT_credentialByUsername_QUERY);
            preparedStatement.setString(1, username);
            ResultSet resultSet = getResultSet(preparedStatement);
            if (resultSet.next()) {
                credential = crm.mapRow(resultSet);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCCredentialRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
        return credential;
    }

    private static ResultSet getResultSet(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
