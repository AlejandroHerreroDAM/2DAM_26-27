package dao.repositories;

import dao.interfaces.MedicalRecordRepository;
import dao.mapper.medicalRecordsQueryMapper;
import dao.model.MedicalRecords;
import dao.repositories.common.SQLQueries;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCMedicalRecordsRepository implements MedicalRecordRepository {

    private final DBConnection db;
    private final medicalRecordsQueryMapper medicalRecordMapper;

    @Inject
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
