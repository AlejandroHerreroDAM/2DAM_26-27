package dao.mapper;

import dao.model.Patient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class patientQueryMapper {

    public List<Patient> mapPatient(ResultSet rs) {

        List<Patient> patients = new ArrayList<>();
        try{
            while (rs.next()){
                Patient patient=new Patient();
                patient.setId(rs.getInt("patient_id"));
                patient.setName(rs.getString("name"));
                java.sql.Date birthDate = rs.getDate("date_of_birth");
                patient.setBirthDate(birthDate == null ? null : birthDate.toLocalDate());
                patient.setPhone(rs.getString("phone"));
                patients.add(patient);
            }
        }catch (SQLException ex){
            Logger.getLogger(patientQueryMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return patients;
    }


}
