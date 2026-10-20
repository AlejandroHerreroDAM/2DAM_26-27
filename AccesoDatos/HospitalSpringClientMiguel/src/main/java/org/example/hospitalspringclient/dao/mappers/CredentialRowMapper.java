package org.example.hospitalspringclient.dao.mappers;


import org.example.hospitalspringclient.dao.model.Credential;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CredentialRowMapper {
    public Credential mapRow(ResultSet rs) throws SQLException {
        Credential credential = new Credential();
        credential.setUsername(rs.getString("username"));
        credential.setPassword(rs.getString("password"));
        return credential;
    }
}
