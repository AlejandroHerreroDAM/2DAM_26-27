package org.hospitalspringclient.dao.mapper;


import org.hospitalspringclient.dao.model.MedicalRecords;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class medicalRecordsQueryMapper {
    public List<MedicalRecords> mapMedicalRecords(ResultSet rs){

        List<MedicalRecords> medicalRecords = new ArrayList<>();
        try{
            while (rs.next()){
                MedicalRecords medicalRecord = new MedicalRecords();
                medicalRecord.setId(rs.getInt("record_id"));
                medicalRecord.setPatientId(rs.getInt("patient_id"));
                medicalRecord.setDoctorId(rs.getInt("doctor_id"));
                medicalRecord.setDiagnosis(rs.getString("diagnosis"));
                medicalRecord.setAddmisionDate(rs.getDate("admission_date").toLocalDate());
                medicalRecords.add(medicalRecord);
            }
        }catch (SQLException ex){
            Logger.getLogger(patientQueryMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return medicalRecords;
    }
}
