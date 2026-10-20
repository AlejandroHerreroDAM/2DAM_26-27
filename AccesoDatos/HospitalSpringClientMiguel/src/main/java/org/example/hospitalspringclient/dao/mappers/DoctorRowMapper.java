package org.example.hospitalspringclient.dao.mappers;



import org.example.hospitalspringclient.dao.model.Doctor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DoctorRowMapper {
    public Doctor mapRow(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getInt("doctor_id"));
        doctor.setName(rs.getString("name"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setPhone(rs.getString("phone"));
        return doctor;
    }
}
