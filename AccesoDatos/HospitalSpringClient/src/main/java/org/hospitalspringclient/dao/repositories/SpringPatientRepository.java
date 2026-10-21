package org.hospitalspringclient.dao.repositories;


import org.hospitalspringclient.common.Constants;
import org.hospitalspringclient.dao.interfaces.PatientRepository;
import org.hospitalspringclient.dao.mapper.patientQueryMapper;
import org.hospitalspringclient.dao.model.Patient;
import org.hospitalspringclient.dao.model.UserLogin;
import org.hospitalspringclient.dao.repositories.common.SQLQueries;
import org.hospitalspringclient.domain.exceptions.DatabaseError;
import org.hospitalspringclient.domain.exceptions.DuplicatedUsernameException;
import org.hospitalspringclient.domain.exceptions.ForeignKeyException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class SpringPatientRepository implements PatientRepository {

    private final patientQueryMapper patientMapper;
    private final JdbcClient jdbcClient;

    public SpringPatientRepository(patientQueryMapper mapPatient, JdbcClient jdbcClient) {
        this.patientMapper = mapPatient;
        this.jdbcClient = jdbcClient;
    }

    public List<Patient> getAll() {
//        List<Patient> patients = new ArrayList<>();
//        try (Connection con = db.getConnection();
//             Statement statement = con.createStatement()) {
//
//            ResultSet rs = statement.executeQuery(SQLQueries.GET_ALL_PATIENTS);
//            patients = patientMapper.mapPatient(rs);
//        } catch (SQLException ex) {
//            Logger.getLogger(SpringPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
//
//        }
        return jdbcClient.sql(SQLQueries.GET_ALL_PATIENTS).query(patientMapper).list();
    }


    @Override
    public int add(Patient patient, UserLogin userLogin) {
        try (Connection connection = db.getConnection()) {
            try {
                connection.setAutoCommit(false);

                int patientId;
                try (PreparedStatement patientStatement = connection.prepareStatement(
                        SQLQueries.ADD_PATIENT, Statement.RETURN_GENERATED_KEYS)) {
                    patientStatement.setString(1, patient.getName());
                    setDate(patientStatement, 2, patient.getBirthDate());
                    patientStatement.setString(3, patient.getPhone());
                    patientStatement.executeUpdate();

                    try (ResultSet generatedKeys = patientStatement.getGeneratedKeys()) {
                        if (!generatedKeys.next()) {
                            throw new DatabaseError(Constants.DATABASE_ERROR);
                        }
                        patientId = generatedKeys.getInt(1);
                    }
                }

                try (PreparedStatement credentialStatement = connection.prepareStatement(
                        SQLQueries.INSERT_CREDENTIAL)) {
                    credentialStatement.setString(1, userLogin.getUsername());
                    credentialStatement.setString(2, userLogin.getPassword());
                    credentialStatement.setInt(3, patientId);
                    credentialStatement.executeUpdate();
                }

                connection.commit();
                return patientId;
            } catch (SQLIntegrityConstraintViolationException ex) {
                connection.rollback();
                throw new DuplicatedUsernameException(Constants.DUPLICATED_USERNAME);
            } catch (SQLException ex) {
                connection.rollback();
                throw new DatabaseError(Constants.DATABASE_ERROR);
            } catch (RuntimeException ex) {
                connection.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }

    @Override
    public void update(Patient patient) {
        jdbcClient.sql(SQLQueries.UPDATE_PATIENT).param("name", patient.getName()).param("date_of_birth", patient.getBirthDate()).param("phone", patient.getPhone()).param("patient_id", patient.getId()).update();


//        try (Connection con = db.getConnection();
//             PreparedStatement statement = con.prepareStatement(SQLQueries.UPDATE_PATIENT)) {
//            statement.setString(1, patient.getName());
//            setDate(statement, 2, patient.getBirthDate());
//            statement.setString(3, patient.getPhone());
//            statement.setInt(4, patient.getId());
//            statement.executeUpdate();
//        } catch (SQLException ex) {
//            Logger.getLogger(SpringPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    @Transactional
    public void delete(int idDelete, boolean confirm) {

        if (confirm) {
            medRecordRepository.deleteByPatient(idDelete);
        }

        paymentsRepository.deleteByPatient(idDelete);
        appointmentRepository.deleteByPatient(idDelete);
        credentialRepository.deleteByPatient(idDelete);
        try {
            jdbcClient.sql(SQLQueries.DELETE_PATIENT).param("id", idDelete).update();
        } catch (DataIntegrityViolationException e) {
            Logger.getLogger((SpringPatientRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new ForeignKeyException();
        }
    }

    private void setDate(PreparedStatement statement, int index, java.time.LocalDate date) {
            throws SQLException {
            if (date == null) {
                throw new IllegalArgumentException(Constants.REQUIRED_BIRTH_DATE);
            }
            statement.setDate(index, Date.valueOf(date));
        }

    }
}

