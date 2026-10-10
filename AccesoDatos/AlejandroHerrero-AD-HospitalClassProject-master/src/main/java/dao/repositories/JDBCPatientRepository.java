package dao.repositories;

import common.Constants;
import dao.interfaces.PatientRepository;
import dao.mapper.patientQueryMapper;
import dao.model.Patient;
import dao.repositories.common.SQLQueries;
import domain.exceptions.DatabaseError;
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

            ResultSet rs = statement.executeQuery(SQLQueries.SELECT_AllPatients_QUERY);
            patients = patientMapper.mapPatient(rs);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);

        }
        return patients;
    }

    @Override
    public int add(Patient patient) {
        try (Connection con = db.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     SQLQueries.INSERT_PATIENT_WITH_AUTOINCREMENTAL_ID,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, patient.getName());
            setDate(statement, 2, patient.getBirthDate());
            statement.setString(3, patient.getPhone());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
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
                    try (PreparedStatement preparedStatement1 = connection.prepareStatement(SQLQueries.DELETE_MEDICATIONS_PATIENT);
                         PreparedStatement preparedStatement2 = connection.prepareStatement(SQLQueries.DELETE_MEDRECORDS_PATIENT);
                         PreparedStatement preparedStatement3 = connection.prepareStatement(SQLQueries.DELETE_APPOINTMENTS_PATIENT);
                         PreparedStatement preparedStatement4 = connection.prepareStatement(SQLQueries.DELETE_PAYMENTS_PATIENT)) {

                        preparedStatement1.setInt(1, idDelete);
                        preparedStatement2.setInt(1, idDelete);
                        preparedStatement3.setInt(1, idDelete);
                        preparedStatement4.setInt(1, idDelete);

                        preparedStatement1.executeUpdate();
                        preparedStatement2.executeUpdate();
                        preparedStatement3.executeUpdate();
                        preparedStatement4.executeUpdate();
                    }

                    try (PreparedStatement preparedStatement5 = connection.prepareStatement(SQLQueries.DELETE_CREDENTIALS_PATIENT);
                         PreparedStatement preparedStatement6 = connection.prepareStatement(SQLQueries.DELETE_PATIENT)) {
                        preparedStatement5.setInt(1, idDelete);
                        preparedStatement6.setInt(1, idDelete);

                        preparedStatement5.executeUpdate();

                        int affectedRows = preparedStatement6.executeUpdate();

                        if (affectedRows == 0) {
                            System.out.println("Error when deleting patient: " + idDelete);
                        }
                    }
                    connection.commit();

                }
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
            throw new IllegalArgumentException("La fecha de nacimiento del paciente es obligatoria");
        }
        statement.setDate(index, Date.valueOf(date));
    }


}
