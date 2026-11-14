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
import domain.model.BackupRow;
import domain.model.BackupValue;
import domain.model.PatientBackup;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCPatientRepository implements PatientRepository {
    private static final List<String> BACKUP_TABLES = List.of(
            "patients", "medical_records", "prescribed_medications", "appointments", "patient_payments");

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
                         PreparedStatement preparedStatementMedication = connection.prepareStatement(SQLQueries.DELETE_MEDICATIONS_PATIENT);
                         PreparedStatement preparedStatement3 = connection.prepareStatement(SQLQueries.DELETE_APPOINTMENTS_PATIENT);
                         PreparedStatement preparedStatement4 = connection.prepareStatement(SQLQueries.DELETE_PAYMENTS_PATIENT)) {

                        preparedStatement2.setInt(1, idDelete);
                        preparedStatementMedication.setInt(1, idDelete);
                        preparedStatement3.setInt(1, idDelete);
                        preparedStatement4.setInt(1, idDelete);

                        preparedStatementMedication.executeUpdate();
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

    @Override
    public PatientBackup createBackup(int patientId) {
        try (Connection connection = db.getConnection()) {
            PatientBackup backup = new PatientBackup();
            List<BackupRow> patients = readRows(connection,
                    "SELECT * FROM patients WHERE patient_id = ?", patientId);
            if (patients.isEmpty()) {
                throw new DatabaseError(Constants.PATIENT_DELETE_ERROR + patientId);
            }
            backup.getTables().put("patients", patients);
            List<BackupRow> records = readRows(connection,
                    "SELECT * FROM medical_records WHERE patient_id = ?", patientId);
            backup.getTables().put("medical_records", records);
            backup.getTables().put("prescribed_medications",
                    readRowsForRecords(connection, records, "prescribed_medications"));
            backup.getTables().put("appointments",
                    readRows(connection, "SELECT * FROM appointments WHERE patient_id = ?", patientId));
            backup.getTables().put("patient_payments",
                    readRows(connection, "SELECT * FROM patient_payments WHERE patient_id = ?", patientId));
            return backup;
        } catch (SQLException e) {
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }

    @Override
    public void restore(PatientBackup backup) {
        try (Connection connection = db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                for (String table : BACKUP_TABLES) {
                    for (BackupRow row : backup.getTables().getOrDefault(table, List.of())) {
                        insertRow(connection, table, row);
                    }
                }
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                if (e instanceof DatabaseError databaseError) {
                    throw databaseError;
                }
                throw new DatabaseError(Constants.DATABASE_ERROR);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DatabaseError(Constants.DATABASE_ERROR);
        }
    }

    private List<BackupRow> readRows(Connection connection, String sql, int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapRows(resultSet);
            }
        }
    }

    private List<BackupRow> readRowsForRecords(Connection connection, List<BackupRow> records,
                                               String table) throws SQLException {
        if (records.isEmpty()) {
            return List.of();
        }
        String placeholders = String.join(",", java.util.Collections.nCopies(records.size(), "?"));
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM " + table + " WHERE record_id IN (" + placeholders + ")")) {
            int index = 1;
            for (BackupRow record : records) {
                statement.setInt(index++, Integer.parseInt(record.getValues().get("record_id").getValue()));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapRows(resultSet);
            }
        }
    }

    private List<BackupRow> mapRows(ResultSet resultSet) throws SQLException {
        List<BackupRow> rows = new ArrayList<>();
        ResultSetMetaData metadata = resultSet.getMetaData();
        while (resultSet.next()) {
            BackupRow row = new BackupRow();
            for (int i = 1; i <= metadata.getColumnCount(); i++) {
                Object value = resultSet.getObject(i);
                row.put(metadata.getColumnName(i),
                        new BackupValue(Integer.toString(metadata.getColumnType(i)),
                                value == null ? null : value.toString()));
            }
            rows.add(row);
        }
        return rows;
    }

    private void insertRow(Connection connection, String table, BackupRow row) throws SQLException {
        if (!BACKUP_TABLES.contains(table) || row.getValues().isEmpty()
                || row.getValues().keySet().stream().anyMatch(column -> !column.matches("[A-Za-z0-9_]+"))) {
            throw new DatabaseError("Invalid patient backup table");
        }
        String columns = String.join(",", row.getValues().keySet());
        String placeholders = String.join(",", java.util.Collections.nCopies(row.getValues().size(), "?"));
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO " + table + " (" + columns + ") VALUES (" + placeholders + ")")) {
            int index = 1;
            for (BackupValue value : row.getValues().values()) {
                setBackupValue(statement, index++, value);
            }
            statement.executeUpdate();
        }
    }

    private void setBackupValue(PreparedStatement statement, int index, BackupValue value)
            throws SQLException {
        if (value.getValue() == null) {
            statement.setNull(index, Integer.parseInt(value.getType()));
            return;
        }
        int type = Integer.parseInt(value.getType());
        if (type == Types.INTEGER || type == Types.SMALLINT || type == Types.TINYINT) {
            statement.setInt(index, Integer.parseInt(value.getValue()));
        } else if (type == Types.BIGINT) {
            statement.setLong(index, Long.parseLong(value.getValue()));
        } else if (type == Types.DATE) {
            statement.setDate(index, Date.valueOf(value.getValue()));
        } else if (type == Types.TIMESTAMP || type == Types.TIMESTAMP_WITH_TIMEZONE) {
            String timestamp = value.getValue().replace('T', ' ');
            if (timestamp.length() == 16) {
                timestamp += ":00";
            }
            statement.setTimestamp(index, Timestamp.valueOf(timestamp));
        } else if (type == Types.DECIMAL || type == Types.NUMERIC) {
            statement.setBigDecimal(index, new java.math.BigDecimal(value.getValue()));
        } else {
            statement.setString(index, value.getValue());
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
