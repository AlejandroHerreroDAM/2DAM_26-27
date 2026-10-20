package org.example.hospitalspringclient.dao.repositories.JDBC;
import org.example.hospitalspringclient.common.Constants;
import org.example.hospitalspringclient.dao.mappers.MedRecordRowMapper;
import org.example.hospitalspringclient.dao.model.MedicalRecord;
import org.example.hospitalspringclient.dao.repositories.MedRecordRepository;
import org.example.hospitalspringclient.dao.repositories.common.SQLQueries;
import org.example.hospitalspringclient.domain.errors.DatabaseError;
import org.example.hospitalspringclient.domain.errors.ForeignKeyError;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class JDBCMedRecordRepository implements MedRecordRepository {
    private final DBConnection db;
    private final MedRecordRowMapper mrrm;

    public JDBCMedRecordRepository(DBConnection db, MedRecordRowMapper mrrm) {
        this.db = db;
        this.mrrm = mrrm;
    }


    @Override
    public List<MedicalRecord> getAllById(int patientId) {
        List<MedicalRecord> medRecords = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(SQLQueries.SELECT_medRecordsByID_QUERY)) {
            ps.setInt(1, patientId);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    medRecords.add(mrrm.mapRow(resultSet));
                }
                if (medRecords.isEmpty()) {
                    throw new DatabaseError(Constants.MISSING_USER);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        } catch (DatabaseError ex) {
            throw ex;
        }
        return medRecords;
    }

    @Override
    public int add(MedicalRecord medRecord) {
        int medRecordId = 0;
        try (Connection c = db.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(SQLQueries.INSERT_MEDRECORD_WITH_AUTOINCREMENTAL_ID, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement ps1 = c.prepareStatement(SQLQueries.INSERT_MEDICATION)
            ) {
                ps.setInt(1, medRecord.getPatient_id());
                ps.setInt(2, medRecord.getDoctor_id());
                ps.setString(3, medRecord.getDiagnosis());
                ps.setDate(4, Date.valueOf(medRecord.getAdmission_date()));
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new DatabaseError(Constants.DATABASE_ERROR);
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        medRecordId = rs.getInt(1);
                    } else {
                        throw new DatabaseError(Constants.DATABASE_ERROR);
                    }
                }
                ps1.setInt(1, medRecordId);
                ps1.setString(2, String.valueOf(medRecord.getMedications()));
                ps1.setString(3, "5ml");
                int affectedRows1 = ps1.executeUpdate();
                if (affectedRows1 == 0) {
                    throw new DatabaseError(Constants.DATABASE_ERROR);
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, e);
                throw new DatabaseError(Constants.DATABASE_ERROR);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
        return medRecordId;
    }

    @Override
    public void update(MedicalRecord medRecord) {
        try (Connection c = db.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(SQLQueries.UPDATE_MEDRECORD);
                 PreparedStatement ps1 = c.prepareStatement(SQLQueries.UPDATE_MEDICATIONS)) {
                ps.setString(1, medRecord.getDiagnosis());
                ps.setDate(2, Date.valueOf(medRecord.getAdmission_date()));
                ps.setInt(3, medRecord.getId());
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new DatabaseError(Constants.MISSING_MEDRECORD);
                }
                ps1.setString(1, String.valueOf(medRecord.getMedications()));
                ps1.setInt(2, medRecord.getId());
                int affectedRows1 = ps1.executeUpdate();
                if (affectedRows1 == 0) {
                    throw new DatabaseError(Constants.MISSING_MEDICATIONS);
                }
                c.commit();
            } catch (DatabaseError e) {
                c.rollback();
                Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, e);
                throw e;

            } catch (SQLException e) {
                c.rollback();
                Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, e);
                throw new DatabaseError(Constants.DATABASE_ERROR);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }

    @Override
    public void delete(int idDelete, boolean confirmation) {
        try (Connection c = db.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(SQLQueries.DELETE_MEDRECORD);
                 PreparedStatement ps1 = c.prepareStatement(SQLQueries.DELETE_MEDICATIONS)) {
                if (confirmation) {
                    ps1.setInt(1, idDelete);
                    int affectedRows = ps1.executeUpdate();
                    if (affectedRows == 0) {
                        throw new DatabaseError(Constants.MISSING_MEDICATIONS);
                    }
                }
                ps.setInt(1, idDelete);
                int affectedRows1 = ps.executeUpdate();
                if (affectedRows1 == 0) {
                    throw new DatabaseError(Constants.MISSING_MEDRECORD);
                }
                c.commit();
            }catch (SQLIntegrityConstraintViolationException e){
                c.rollback();
                Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, e);
                throw new ForeignKeyError(Constants.MEDICATION_EXIST);
            }
            catch (DatabaseError e) {
                c.rollback();
                Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, e);
                throw e;
            } catch (SQLException e) {
                c.rollback();
                Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, e);
                throw new DatabaseError(Constants.DATABASE_ERROR);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }
}
