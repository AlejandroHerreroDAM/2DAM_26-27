package org.hospitalspringclient.common;

public class Constants {
    public static final String DATABASE_ERROR = "Error in the database";
    public static final String DUPLICATED_USERNAME = "El username ya está registrado";
    public static final String NULL_PATIENT = "El paciente no puede ser null";
    public static final String REQUIRED_PATIENT_NAME = "El nombre del paciente es obligatorio";
    public static final String REQUIRED_BIRTH_DATE = "La fecha de nacimiento del paciente es obligatoria";
    public static final String REQUIRED_CREDENTIALS = "El username y la password son obligatorios";
    public static final String CRITICAL_GET_PATIENTS = "Critical error with GetAllPatients: ";
    public static final String CRITICAL_CREATE_PATIENT = "Critical error with patient creation: ";
    public static final String CRITICAL_DELETE_PATIENT = "Critical error with DeletePatient ";
    public static final String PATIENT_CREDENTIALS_ERROR = "Patient and credentials creation error";
    public static final String DELETE_PATIENT_ERROR = "PatientService.deletePatient error";
    public static final String UNEXPECTED_ERROR = "Unexpected Error";
    public static final String MEDICAL_RECORDS_CONFIRMATION = "There are medical records related to the patient. Are you sure you want to delete this patient? (Y/N)";
    public static final String DELETE_CANCELLED = "Delete cancelled.";
    public static final String DATABASE_ERROR_MESSAGE = "Database Error";
    public static final String PATIENT_DELETE_ERROR = "Error when deleting patient: ";
    public static final String APP_TITLE = "Hospital CRUD application";
    public static final String LOGIN_PROMPT = "Please login to continue";
    public static final String LOGIN_SUCCESS = "Login successful!";
    public static final String LOGIN_FAILED = "Login failed!";
    public static final String MENU = """
                            Hospital CRUD Application
                            1. Get all Patients
                            2. Add Patient
                            3. Update Patient
                            4. Delete Patient
                            5. Get all Doctors
                            6. Get all Medical Records by patient
                            7. Add Medical Record
                            8. Update Medical Record
                            9. Delete Medical Record
                            10. Exit
                            """;
    public static final String ENTER_CHOICE = "Enter your choice: ";
    public static final String PATIENT_ID = "Patient id: ";
    public static final String MEDICAL_RECORD_ID = "Medical record id: ";
    public static final String PATIENT_ADDED = "Patient added with id: ";
    public static final String PATIENT_UPDATED = "Patient updated.";
    public static final String PATIENT_DELETED = "Patient deleted.";
    public static final String MEDICAL_RECORD_ADDED = "Medical record added with id: ";
    public static final String MEDICAL_RECORD_UPDATED = "Medical record updated.";
    public static final String MEDICAL_RECORD_DELETED = "Medical record deleted.";
    public static final String EXITING = "Exiting...";
    public static final String INVALID_OPTION = "Invalid option! Please try again.";
    public static final String USERNAME = "Username: ";
    public static final String PASSWORD = "Password: ";
    public static final String NAME = "Name: ";
    public static final String BIRTH_DATE = "Date of birth (yyyy-MM-dd): ";
    public static final String PHONE = "Phone: ";
    public static final String DOCTOR_ID = "Doctor id: ";
    public static final String DIAGNOSIS = "Diagnosis: ";
    public static final String ADMISSION_DATE = "Admission date (yyyy-MM-dd): ";
}
