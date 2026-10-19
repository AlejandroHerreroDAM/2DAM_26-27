package org.hospitalspringclient.dao.repositories;


import org.hospitalspringclient.dao.interfaces.MedicalRecordRepository;
import org.hospitalspringclient.dao.mapper.medicalRecordsQueryMapper;
import org.hospitalspringclient.dao.model.MedicalRecords;
import org.hospitalspringclient.dao.repositories.common.SQLQueries;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class JDBCMedicalRecordsRepository implements MedicalRecordRepository {

    private final DBConnection db;
    private final medicalRecordsQueryMapper medicalRecordMapper;

    public JDBCMedicalRecordsRepository(DBConnection db, medicalRecordsQueryMapper medicalRecordMapper) {
        this.db = db;
        this.medicalRecordMapper = medicalRecordMapper;
    }


    @Override
    public List<MedicalRecords> getAllById(int PatientId) {
        List<MedicalRecords> medicalRecords = new ArrayList<>();
        try (Connection con = db.getConnection();
             java.sql.PreparedStatement statement = con.prepareStatement(SQLQueries.GET_MEDRECORDS_BY_PATIENT)) {
            statement.setInt(1, PatientId);
            try (ResultSet rs = statement.executeQuery()) {
                medicalRecords = medicalRecordMapper.mapMedicalRecords(rs);
            }
        } catch (SQLException ex){
            Logger.getLogger(JDBCMedicalRecordsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return medicalRecords;
    }

    @Override
    public int add(MedicalRecords medicalRecords) {
        try (Connection con = db.getConnection();
             java.sql.PreparedStatement statement = con.prepareStatement(
                     SQLQueries.ADD_MEDRECORD, Statement.RETURN_GENERATED_KEYS)) {
            setRecordParameters(statement, medicalRecords, false);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCMedicalRecordsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public void update(MedicalRecords medicalRecords) {
        try (Connection con = db.getConnection();
             java.sql.PreparedStatement statement = con.prepareStatement(SQLQueries.UPDATE_MEDRECORD)) {
            setRecordParameters(statement, medicalRecords, true);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCMedicalRecordsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(int idDelete) {
        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);
            try (java.sql.PreparedStatement record =
                         con.prepareStatement(SQLQueries.DELETE_MEDRECORD)) {
                record.setInt(1, idDelete);
                record.executeUpdate();
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCMedicalRecordsRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setRecordParameters(java.sql.PreparedStatement statement,
                                     MedicalRecords medicalRecord,
                                     boolean includeId) throws SQLException {
        statement.setInt(1, medicalRecord.getPatientId());
        statement.setInt(2, medicalRecord.getDoctorId());
        statement.setString(3, medicalRecord.getDiagnosis());
        statement.setDate(4, Date.valueOf(medicalRecord.getAddmisionDate()));
        if (includeId) {
            statement.setInt(5, medicalRecord.getId());
        }
    }

}
