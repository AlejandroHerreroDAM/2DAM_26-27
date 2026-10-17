package org.hospitalspringclient.dao.repositories;

import org.hospitalspringclient.dao.interfaces.DoctorRepository;
import org.hospitalspringclient.dao.mapper.doctorQueryMapper;
import org.hospitalspringclient.dao.model.Doctor;
import org.hospitalspringclient.dao.repositories.common.SQLQueries;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class JDBCDoctorRepository implements DoctorRepository {
    private final DBConnection db;
    private final doctorQueryMapper doctorMapper;

    public JDBCDoctorRepository(DBConnection db, doctorQueryMapper doctorMapper) {
        this.db = db;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public List<Doctor> getAll() {
        try (Connection con = db.getConnection();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(SQLQueries.GET_ALL_DOCTORS)) {
            return doctorMapper.mapDoctor(rs);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, ex);
            return List.of();
        }
    }
}
