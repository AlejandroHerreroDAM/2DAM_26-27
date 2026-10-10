package dao.mapper;

import dao.model.Doctor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class doctorQueryMapper {

    public List<Doctor> mapDoctor(ResultSet rs) {
        List<Doctor> doctors = new ArrayList<>();
        try {
            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("doctor_id"));
                doctor.setName(rs.getString("name"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setPhone(rs.getString("phone"));
                doctors.add(doctor);
            }
        }catch (SQLException ex) {
            Logger.getLogger(doctorQueryMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doctors;
    }

}
