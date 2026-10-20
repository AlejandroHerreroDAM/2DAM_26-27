package org.example.hospitalspringclient.dao.mappers;



import org.example.hospitalspringclient.dao.model.MedicalRecord;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MedRecordRowMapper {
    public MedicalRecord mapRow(ResultSet rs) throws SQLException {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(rs.getInt("record_id"));
        medicalRecord.setPatient_id(rs.getInt("patient_id"));
        medicalRecord.setDoctor_id(rs.getInt("doctor_id"));
        medicalRecord.setDiagnosis(rs.getString("diagnosis"));
        medicalRecord.setAdmission_date(rs.getDate("admission_date").toLocalDate());
        return medicalRecord;
    }
}
