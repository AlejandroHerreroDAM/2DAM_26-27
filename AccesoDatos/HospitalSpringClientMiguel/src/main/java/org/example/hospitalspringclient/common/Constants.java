package org.example.hospitalspringclient.common;

public class Constants {
public static final String MENU =
        """
        Hospital CRUD Application
        Please log in to continue.
        Username: root
        Password: quevedo2dam
        Connected to DB
        Login successful! Welcome root!
        1. Get all patients
        2. Add Patient
        3. Update Patient
        4. Delete Patient
        5. Get all Doctors
        6. Get all MedRecords by patient
        7. Add MedRecord
        8. Update MedRecord
        9. Delete MedRecord
        10. Exit
        Enter your choice:
        """;
    public static final String LOGIN_MENU =
            """
            Hospital CRUD application
            Please login to continue
            Username:""";
    public static final String DATABASE_ERROR = "Error in database";
    public static final String APP_ERROR = "Critical error with getting all items";
    public static final String LOGIN_ERROR = "Login not successful";
    public static final String APP_ERROR_CRITICAL = "Critical error: ";
    public static final String DUPLICATED_USER_ERROR = "That username already exists. Insert a new one:";
    public static final String MEDRECORD_EXISTS = "There are medical records related to the patient. Are you sure you want to delete this patient? (Y/N)";
    public static final String DELETE_CANCELLED = "Deletion cancelled";
    public static final String DATA_EXISTS =  "There is data related to the item you want to delete";
    public static final String MISSING_USER = "No patient was found with the entered Patient ID";
    public static final String DATE_EXISTS = "The name or phone you entered, already belongs to another patient";
    public static final String MISSING_MEDRECORD = "No medical record was found with the entered Record ID";
    public static final String MISSING_MEDICATIONS = "No prescription was found";
    public static final String MEDICATION_EXIST = "There are medications related to the medical record. Are you sure you want to delete this medical record?";
    public static final String INSERT_PASSWORD = "Password: ";
    public static final String LOGIN_SUCCESSFUL = "Login successful!";
    public static final String LIST_OF_PATIENTS = "List of patients: ";
    public static final String ADD_PATIENT_TITLE = "Add Patient";
    public static final String UPDATE_PATIENT_TITLE = "Update Patient";
    public static final String DELETE_PATIENT_TITLE = "Delete Patient";
    public static final String ENTER_PATIENT_ID = "Enter patient ID";
    public static final String GET_ALL_DOCTORS_TITLE = "Get all Doctors";
    public static final String LIST_OF_DOCTORS = "List of doctors: ";
    public static final String GET_ALL_MED_RECORDS_BY_PATIENT_TITLE = "Get all MedRecords by patient";
    public static final String ADD_MED_RECORD_TITLE = "Add MedRecord";
    public static final String INSERT_PATIENT_ID = "Insert patient ID:";
    public static final String FAILED_MEDRECORD_ERROR = "Error while adding medical record.Try again";
    public static final String UPDATE_MED_RECORD_TITLE = "Update MedRecord";
    public static final String ENTER_MEDICAL_RECORD_ID = "Enter Medical Record ID:";
    public static final String EXITING_APP = "Exiting...";
    public static final String INVALID_OPTION = "Invalid option!";
    public static final String LOGIN_FAILED = "Login failed!";
}
