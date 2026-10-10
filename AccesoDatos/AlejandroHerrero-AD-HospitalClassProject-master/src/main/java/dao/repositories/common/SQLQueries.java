package dao.repositories.common;

import java.security.PublicKey;

public class SQLQueries {
    public static final String SELECT_AllPatients_QUERY = "SELECT * FROM patients";
    public static final String INSERT_PATIENT_WITH_AUTOINCREMENTAL_ID = "INSERT INTO patients (name, date_of_birth, phone) VALUES (?, ?, ?)";
    public static final String UPDATE_PATIENT = "UPDATE patients SET name = ?, date_of_birth = ?, phone = ? WHERE patient_id = ?";
    public static final String DELETE_PATIENT = "DELETE FROM patients WHERE patient_id = ?";
    public static final String SELECT_ALL_DOCTORS_QUERY =
            "SELECT doctor_id, name, specialization, phone FROM doctors";
    public static final String SELECT_CREDENTIAL_BY_USERNAME =
            "SELECT user_id, username, password, patient_id, doctor_id FROM user_login WHERE username = ?";
    public static final String SELECT_AllMedicalRecordsByPatientId_QUERY =
            "SELECT * FROM medical_records WHERE patient_id = ?";
    public static final String DELETE_MEDRECORDS_PATIENT= "DELETE from medical_records where patient_id = ?";
    public static final String DELETE_CREDENTIALS_PATIENT= "DELETE from user_login where patient_id = ?";
    public static final String DELETE_APPOINTMENTS_PATIENT= "DELETE from appointments where patient_id = ?";
    public static final String DELETE_MEDICATIONS_PATIENT= "DELETE from prescribed_medications where patient_id= ?";
    public static final String DELETE_PAYMENTS_PATIENT= "DELETE from paient_payments where patient_id = ?";
    public static final String DELETE_MEDRECORD = "DELETE from medical_records where record_id = ?";
    public static final String DELETE_MEDICATIONS_MEDRECORD = "DELETE from prescribed_medications where record_id = ?";






}