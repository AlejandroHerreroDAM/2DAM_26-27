package org.hospitalspringclient.dao.mapper;


import org.hospitalspringclient.dao.model.UserLogin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class credentialQueryMapper {

    public UserLogin mapCredential(ResultSet rs) {
        UserLogin credential = null;
        try {
            if (rs.next()) {
                credential = new UserLogin();
                credential.setPassword(rs.getString("password"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(credentialQueryMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return credential;
    }
}
