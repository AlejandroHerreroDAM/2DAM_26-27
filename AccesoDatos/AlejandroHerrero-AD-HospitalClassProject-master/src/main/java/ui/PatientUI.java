package ui;

import domain.exceptions.AppException;
import domain.exceptions.DatabaseError;
import domain.exceptions.DuplicatedUsernameException;
import domain.exceptions.ForeignKeyException;
import domain.model.PatientDTO;
import domain.model.CredentialDTO;
import domain.service.PatientService;
import common.Constants;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientUI {
    private final PatientService patientService;

    @Inject
    public PatientUI(PatientService patientService) {
        this.patientService = patientService;

    }

    public List<PatientDTO> getAllPatients() {

        List<PatientDTO> patients = new ArrayList<>();

        try {
            return patientService.getAllPatients();
        } catch (DatabaseError ex) {
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, null, e);
            throw new AppException(Constants.CRITICAL_GET_PATIENTS + e.getMessage());
        }
        return patients;
    }


    public int addPatient(PatientDTO patient, CredentialDTO credential) {
        try {
            return patientService.addPatient(patient, credential);
        } catch (RuntimeException ex) {
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, Constants.PATIENT_CREDENTIALS_ERROR, ex);
            if (ex instanceof DuplicatedUsernameException) {
                throw ex;
            }
            throw new AppException(Constants.CRITICAL_CREATE_PATIENT + ex.getMessage());
        }
    }

    public PatientDTO readPatient(int id) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(Constants.NAME);
        String name = scanner.nextLine();
        System.out.print(Constants.BIRTH_DATE);
        LocalDate birthDate = LocalDate.parse(scanner.nextLine());
        System.out.print(Constants.PHONE);
        String phone = scanner.nextLine();
        return new PatientDTO(id, name, birthDate, phone, 0, null, null);
    }

    public void updatePatient(PatientDTO patient) {
        patientService.updatePatient(patient);
    }

    public void deletePatient(int idPatient, boolean confirmed) {
        Scanner sc = new Scanner(System.in);
        try{
            patientService.deletePatient(idPatient, false);
            System.out.println(Constants.PATIENT_DELETED);
        } catch (ForeignKeyException e){
            System.out.println(Constants.MEDICAL_RECORDS_CONFIRMATION);
            String userAnswer = sc.nextLine().trim();
            if (!userAnswer.isEmpty() && Character.toUpperCase(userAnswer.charAt(0)) == 'Y') {
                patientService.deletePatient(idPatient, true);
                System.out.println(Constants.PATIENT_DELETED);
            } else {
                System.out.println(Constants.DELETE_CANCELLED);
            }
        } catch (DatabaseError e) {
            System.err.println(Constants.DATABASE_ERROR_MESSAGE);
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, Constants.DELETE_PATIENT_ERROR, e);
        } catch (Exception e) {
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, Constants.UNEXPECTED_ERROR, e);
            throw new AppException(Constants.CRITICAL_DELETE_PATIENT);
        }
    }

}
