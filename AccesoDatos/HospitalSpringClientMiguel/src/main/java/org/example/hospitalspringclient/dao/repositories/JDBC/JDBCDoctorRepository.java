package org.example.hospitalspringclient.dao.repositories.JDBC;



import org.example.hospitalspringclient.common.Constants;
import org.example.hospitalspringclient.dao.mappers.DoctorRowMapper;
import org.example.hospitalspringclient.dao.model.Doctor;
import org.example.hospitalspringclient.dao.repositories.DoctorRepository;
import org.example.hospitalspringclient.dao.repositories.common.SQLQueries;
import org.example.hospitalspringclient.domain.errors.DatabaseError;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class JDBCDoctorRepository implements DoctorRepository {
    private final DBConnection db;
    private final DoctorRowMapper drm;

    public JDBCDoctorRepository(DBConnection db, DoctorRowMapper drm) {
        this.db = db;
        this.drm = drm;
    }

    @Override
    public List<Doctor> getAll() {
        List<Doctor> doctors = new ArrayList<>();
        try (Connection con = db.getConnection();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(SQLQueries.SELECT_doctors_QUERY)) {
            while (rs.next()) {
                doctors.add(drm.mapRow(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
        return doctors;
    }
}
