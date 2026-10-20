package org.example.hospitalspringclient.dao.repositories.JDBC;
import org.example.hospitalspringclient.common.Constants;
import org.example.hospitalspringclient.dao.mappers.PatientRowMapper;
import org.example.hospitalspringclient.dao.model.Patient;
import org.example.hospitalspringclient.dao.repositories.PatientRepository;
import org.example.hospitalspringclient.dao.repositories.common.SQLQueries;
import org.example.hospitalspringclient.domain.errors.DatabaseError;
import org.example.hospitalspringclient.domain.errors.DuplicatedUsernameError;
import org.example.hospitalspringclient.domain.errors.ForeignKeyError;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class SpringPatientRepository implements PatientRepository {
    private final DBConnection db;
    private final PatientRowMapper prm;
    private final JdbcClient jdbcClient;

    public SpringPatientRepository(DBConnection db, PatientRowMapper prm, JdbcClient jdbcClient) {
        this.db = db;
        this.prm = prm;
        this.jdbcClient=jdbcClient;
    }

    @Override
    public List<Patient> getAll() {

        return jdbcClient.sql(SQLQueries.SELECT_patients_QUERY).query(prm).list();


        //        List<Patient> patients = new ArrayList<>();
//        try (Connection con = db.getConnection();
//             Statement statement = con.createStatement();
//             ResultSet rs = statement.executeQuery(SQLQueries.SELECT_patients_QUERY)) {
//            while (rs.next()) {
//                patients.add(prm.mapRow(rs));
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(SpringPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
//            throw new DatabaseError(Constants.DATABASE_ERROR);
//        }
//        return patients;
    }

    @Override
    public int add(Patient patient) {
        try (Connection c = db.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps1 = c.prepareStatement(SQLQueries.INSERT_PATIENT_WITH_AUTOINCREMENTAL_ID, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement ps2 = c.prepareStatement(SQLQueries.INSERT_CREDENTIAL)) {
                ps1.setString(1, patient.getName());
                ps1.setDate(2, Date.valueOf(patient.getBirthDate()));
                ps1.setString(3, patient.getPhone());
                int affectedRows = ps1.executeUpdate();
                if (affectedRows == 0) {
                    throw new DatabaseError(Constants.DATABASE_ERROR);
                }
                int idGenerated;
                try (ResultSet rs = ps1.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerated = rs.getInt(1);
                        patient.setId(idGenerated);
                    } else {
                        throw new DatabaseError(Constants.DATABASE_ERROR);
                    }
                }
                ps2.setString(1, patient.getCredential().getUsername());
                ps2.setString(2, patient.getCredential().getPassword());
                ps2.setInt(3, patient.getId());
                ps2.executeUpdate();
                c.commit();
                return idGenerated;
            } catch (SQLIntegrityConstraintViolationException e) {
                c.rollback();
                if (e.getMessage().contains("unique_name_dob")) {
                    Logger.getLogger(SpringPatientRepository.class.getName()).log(Level.SEVERE, null, e);
                    throw new DatabaseError(Constants.DATE_EXISTS);
                } else {
                    Logger.getLogger(SpringPatientRepository.class.getName()).log(Level.SEVERE, null, e);
                    throw new DuplicatedUsernameError(Constants.DUPLICATED_USER_ERROR);
                }

            } catch (SQLException | DatabaseError e) {
                c.rollback();
                Logger.getLogger(SpringPatientRepository.class.getName()).log(Level.SEVERE, null, e);
                throw new DatabaseError(Constants.DATABASE_ERROR);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            Logger.getLogger(SpringPatientRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }

    @Override
    public void update(Patient patient) {
        try (Connection c = db.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(SQLQueries.UPDATE_PATIENT)) {
                ps.setString(1, patient.getName());
                ps.setDate(2, Date.valueOf(patient.getBirthDate()));
                ps.setString(3, patient.getPhone());
                ps.setInt(4, patient.getId());
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new DatabaseError(Constants.MISSING_USER);
                }
                c.commit();
            } catch (SQLIntegrityConstraintViolationException e) {
                c.rollback();
                Logger.getLogger(SpringPatientRepository.class.getName()).log(Level.SEVERE, null, e);
                throw new DatabaseError(Constants.DATE_EXISTS);
            } catch (SQLException e) {
                c.rollback();
                Logger.getLogger(SpringPatientRepository.class.getName()).log(Level.SEVERE, null, e);
                throw new DatabaseError(Constants.DATABASE_ERROR);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            Logger.getLogger(SpringPatientRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }

    @Override
    public void delete(int idDelete, boolean confirmation) {
        try (Connection c = db.getConnection();) {
            c.setAutoCommit(false);
            if (confirmation) {
                try (PreparedStatement preparedStatement = c.prepareStatement(SQLQueries.DELETE_MEDICATION);
                     PreparedStatement preparedStatement1 = c.prepareStatement(SQLQueries.DELETE_ALL_MEDRECORDS);) {
                    preparedStatement.setInt(1, idDelete);
                    preparedStatement1.setInt(1, idDelete);
                    preparedStatement.executeUpdate();
                    preparedStatement1.executeUpdate();
                } catch (SQLIntegrityConstraintViolationException e) {
                    c.rollback();
                    throw new ForeignKeyError(Constants.DATA_EXISTS);
                }
            }
            try (PreparedStatement psPayments = c.prepareStatement(SQLQueries.DELETE_PAYMENTS);
                 PreparedStatement psAppointments = c.prepareStatement(SQLQueries.DELETE_APPOINTMENTS);
                 PreparedStatement psCredential = c.prepareStatement(SQLQueries.DELETE_CREDENTIAL);
                 PreparedStatement psPatient = c.prepareStatement(SQLQueries.DELETE_PATIENT);
            ) {
                psPatient.setInt(1, idDelete);
                psPayments.setInt(1, idDelete);
                psAppointments.setInt(1, idDelete);
                psCredential.setInt(1, idDelete);
                psPayments.executeUpdate();
                psAppointments.executeUpdate();
                psCredential.executeUpdate();
                int affectedRows = psPatient.executeUpdate();
                if (affectedRows == 0) {
                    throw new DatabaseError(Constants.MISSING_USER);
                }
                c.commit();
            } catch (DatabaseError|ForeignKeyError e) {
                c.rollback();
                throw e;
            } catch (SQLIntegrityConstraintViolationException e) {
                c.rollback();
                throw new ForeignKeyError(Constants.MEDRECORD_EXISTS);
            } catch (SQLException e) {
                c.rollback();
                Logger.getLogger(SpringPatientRepository.class.getName()).log(Level.SEVERE, null, e);
                throw new DatabaseError(Constants.DATABASE_ERROR);
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            Logger.getLogger(SpringPatientRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }
}
