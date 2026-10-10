package dao.mapper;

import dao.model.UserLogin;

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
                credential.setId(rs.getInt("user_id"));
                credential.setUsername(rs.getString("username"));
                credential.setPassword(rs.getString("password"));
                credential.setPatientId(rs.getInt("patient_id"));
                credential.setDoctorId(rs.getInt("doctor_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(credentialQueryMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return credential;
    }
}
