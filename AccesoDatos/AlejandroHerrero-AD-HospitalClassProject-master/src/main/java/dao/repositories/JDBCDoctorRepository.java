package dao.repositories;

import dao.interfaces.DoctorRepository;
import dao.mapper.doctorQueryMapper;
import dao.model.Doctor;
import dao.repositories.common.SQLQueries;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCDoctorRepository implements DoctorRepository {
    private final DBConnection db;
    private final doctorQueryMapper doctorMapper;

    @Inject
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
