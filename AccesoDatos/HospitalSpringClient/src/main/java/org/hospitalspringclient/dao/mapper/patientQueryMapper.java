package org.hospitalspringclient.dao.mapper;


import org.hospitalspringclient.dao.model.Patient;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class patientQueryMapper implements RowMapper<Patient> {

    public Patient mapRow(ResultSet rs, int rowNumber) throws SQLException {

        Patient patient = new Patient();
        patient.setId(rs.getInt("patient_id"));
        patient.setName(rs.getString("name"));
        patient.setBirthDate(rs.getDate("date_of_birth").toLocalDate());
        patient.setPhone(rs.getString("phone"));

        return patient;
    }

}
