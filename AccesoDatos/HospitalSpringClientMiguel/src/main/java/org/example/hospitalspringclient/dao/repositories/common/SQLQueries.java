package org.example.hospitalspringclient.dao.repositories.common;

public class SQLQueries {
    public static final String SELECT_patients_QUERY = "select * from patients";
    public static final String SELECT_doctors_QUERY = "select * from doctors";
    public static final String SELECT_credentialByUsername_QUERY = "select * from user_login where username= ?";
    public static final String SELECT_medRecordsByID_QUERY = "select * from medical_records where patient_id= ?";
    public static final String UPDATE_PATIENT = "UPDATE patients SET name = ?, date_of_birth = ?, phone = ? WHERE patient_id = ?";
    public static final String INSERT_PATIENT_WITH_AUTOINCREMENTAL_ID = "INSERT INTO patients (name, date_of_birth, phone) VALUES (?,?,?)";
    public static final String INSERT_MEDRECORD_WITH_AUTOINCREMENTAL_ID = "INSERT INTO medical_records (patient_id, doctor_id, diagnosis,admission_date) VALUES (?,?,?,?)";
    public static final String INSERT_MEDICATION = "INSERT INTO prescribed_medications (record_id, medication_name, dosage) VALUES (?,?,?)";
    public static final String DELETE_PATIENT = "delete from patients where patient_id = ?";
    public static final String INSERT_CREDENTIAL = "insert into user_login (username, password,patient_id) values (?,?,?)";
    public static final String DELETE_MEDICATION = "delete from prescribed_medications where record_id in (select record_id from medical_records where patient_id = ?)";
    public static final String DELETE_PAYMENTS = "delete from patient_payments where patient_id = ?";
    public static final String DELETE_APPOINTMENTS = "delete from appointments where patient_id = ?";
    public static final String DELETE_CREDENTIAL = "delete from user_login where patient_id = ?";
    public static final String DELETE_ALL_MEDRECORDS = "delete from medical_records where patient_id = ?";
    public static final String DELETE_MEDRECORD = "delete from medical_records where record_id = ?";
    public static final String DELETE_MEDICATIONS = "delete from prescribed_medications where record_id = ?";
    public static final String UPDATE_MEDRECORD = "UPDATE medical_records SET diagnosis = ?, admission_date = ? WHERE record_id = ?";
    public static final String UPDATE_MEDICATIONS = "UPDATE prescribed_medications SET medication_name = ? WHERE record_id = ?";
}

