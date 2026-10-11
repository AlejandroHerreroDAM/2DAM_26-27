package dao.repositories;

import common.Constants;
import dao.interfaces.PatientRepository;
import dao.mapper.patientQueryMapper;
import dao.model.Patient;
import dao.model.UserLogin;
import dao.repositories.common.SQLQueries;
import domain.exceptions.DatabaseError;
import domain.exceptions.DuplicatedUsernameException;
import domain.exceptions.ForeignKeyException;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCPatientRepository implements PatientRepository {

    private final DBConnection db;
    private final patientQueryMapper patientMapper;

    @Inject
    public JDBCPatientRepository(DBConnection db, patientQueryMapper mapPatient) {
        this.db = db;
        this.patientMapper = mapPatient;
    }

    public List<Patient> getAll() {
        List<Patient> patients = new ArrayList<>();
        try (Connection con = db.getConnection();
             Statement statement = con.createStatement()) {

            ResultSet rs = statement.executeQuery(SQLQueries.GET_ALL_PATIENTS);
            patients = patientMapper.mapPatient(rs);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);

        }
        return patients;
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
        try (Connection con = db.getConnection();
             PreparedStatement statement = con.prepareStatement(SQLQueries.UPDATE_PATIENT)) {
            statement.setString(1, patient.getName());
            setDate(statement, 2, patient.getBirthDate());
            statement.setString(3, patient.getPhone());
            statement.setInt(4, patient.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(int idDelete, boolean confirm) {
        try (Connection connection = db.getConnection()) {
            try {
                connection.setAutoCommit(false);
                if (confirm) {
                    try (PreparedStatement preparedStatement2 = connection.prepareStatement(SQLQueries.DELETE_MEDRECORDS_PATIENT);
                         PreparedStatement preparedStatement3 = connection.prepareStatement(SQLQueries.DELETE_APPOINTMENTS_PATIENT);
                         PreparedStatement preparedStatement4 = connection.prepareStatement(SQLQueries.DELETE_PAYMENTS_PATIENT)) {

                        preparedStatement2.setInt(1, idDelete);
                        preparedStatement3.setInt(1, idDelete);
                        preparedStatement4.setInt(1, idDelete);

                        preparedStatement2.executeUpdate();
                        preparedStatement3.executeUpdate();
                        preparedStatement4.executeUpdate();
                    }

                }

                try (PreparedStatement preparedStatement5 = connection.prepareStatement(SQLQueries.DELETE_CREDENTIALS_PATIENT);
                     PreparedStatement preparedStatement6 = connection.prepareStatement(SQLQueries.DELETE_PATIENT)) {
                    preparedStatement5.setInt(1, idDelete);
                    preparedStatement6.setInt(1, idDelete);

                    preparedStatement5.executeUpdate();
                    int affectedRows = preparedStatement6.executeUpdate();
                    if (affectedRows == 0) {
                        connection.rollback();
                        throw new DatabaseError(Constants.PATIENT_DELETE_ERROR + idDelete);
                    }
                }
                connection.commit();

            } catch (SQLIntegrityConstraintViolationException e){

                connection.rollback();
                e.printStackTrace(System.err);
                throw new ForeignKeyException();
            } catch (SQLException e){

                connection.rollback();
                e.printStackTrace(System.err);
                throw new DatabaseError(Constants.DATABASE_ERROR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }

    private void setDate(PreparedStatement statement, int index, java.time.LocalDate date)
            throws SQLException {
        if (date == null) {
            throw new IllegalArgumentException(Constants.REQUIRED_BIRTH_DATE);
        }
        statement.setDate(index, Date.valueOf(date));
    }


}
