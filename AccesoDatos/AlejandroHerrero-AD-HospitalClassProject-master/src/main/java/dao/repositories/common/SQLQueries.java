package dao.repositories.common;

public class SQLQueries {
    public static final String GET_ALL_PATIENTS = "SELECT * FROM patients";
    public static final String GET_USER_BY_USERNAME = "SELECT password FROM user_login WHERE username = ?";
    public static final String GET_ALL_DOCTORS = "SELECT * FROM doctors";
    public static final String GET_MEDRECORDS_BY_PATIENT = "SELECT record_id, patient_id, doctor_id, diagnosis, admission_date FROM medical_records WHERE patient_id = ?";
    public static final String ADD_PATIENT = "INSERT INTO patients (name, date_of_birth, phone) VALUES (?, ?, ?)";
    public static final String INSERT_CREDENTIAL = "INSERT INTO user_login (username, password, patient_id) VALUES (?, ?, ?)";
    public static final String ADD_MEDRECORD = "INSERT INTO medical_records (patient_id, doctor_id, diagnosis, admission_date) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_PATIENT = "UPDATE patients SET name = ?, date_of_birth = ?, phone = ? WHERE patient_id = ?";
    public static final String UPDATE_MEDRECORD = "UPDATE medical_records SET patient_id = ?, doctor_id = ?, diagnosis = ?, admission_date = ? WHERE record_id = ?";
    public static final String DELETE_PATIENT = "DELETE FROM patients WHERE patient_id = ?";
    public static final String DELETE_MEDRECORDS_PATIENT = "DELETE FROM medical_records WHERE patient_id = ?";
    public static final String DELETE_CREDENTIALS_PATIENT = "DELETE FROM user_login WHERE patient_id = ?";
    public static final String DELETE_APPOINTMENTS_PATIENT = "DELETE FROM appointments WHERE patient_id = ?";
    public static final String DELETE_PAYMENTS_PATIENT = "DELETE FROM patient_payments WHERE patient_id = ?";
    public static final String DELETE_MEDRECORD = "DELETE FROM medical_records WHERE record_id = ?";
}
