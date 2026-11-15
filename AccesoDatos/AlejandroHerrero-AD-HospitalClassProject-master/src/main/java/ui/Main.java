package ui;

import domain.exceptions.AppException;
import domain.exceptions.DuplicatedUsernameException;
import domain.model.CredentialDTO;
import domain.model.DoctorDTO;
import domain.model.MedicalRecordDTO;
import domain.model.PatientDTO;
import common.Constants;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        try {
            SeContainerInitializer initializer = SeContainerInitializer.newInstance();
            final SeContainer container = initializer.initialize();
            PatientUI patientui = container.select(PatientUI.class).get();
            DoctorUI doctorUI = container.select(DoctorUI.class).get();
            MedicalRecordUI medicalRecordUI = container.select(MedicalRecordUI.class).get();
            CredentialUI credentialUI = container.select(CredentialUI.class).get();

            System.out.println(Constants.APP_TITLE);
            System.out.println(Constants.LOGIN_PROMPT);

            Scanner sc = new Scanner(System.in);
            if (credentialUI.login(credentialUI.readCredential())) {
                System.out.println(Constants.LOGIN_SUCCESS);
                int option;

                do {
                    System.out.println(Constants.MENU);

                    System.out.print(Constants.ENTER_CHOICE);
                    option = sc.nextInt();
                    sc.nextLine();

                    switch (option) {
                        case 1:
                            for (PatientDTO patient : patientui.getAllPatients()) {
                                System.out.println(patient);
                            }
                            break;

                        case 2:
                            PatientDTO newPatient = patientui.readPatient(0);
                            CredentialDTO newCredential = credentialUI.readCredential();
                            int newId = patientui.addPatient(newPatient, newCredential);
                            System.out.println(Constants.PATIENT_ADDED + newId);
                            break;

                        case 3:
                            System.out.print(Constants.PATIENT_ID);
                            int updateId = sc.nextInt();
                            sc.nextLine();
                            patientui.updatePatient(patientui.readPatient(updateId));
                            System.out.println(Constants.PATIENT_UPDATED);
                            break;

                        case 4:
                            System.out.print(Constants.PATIENT_ID);
                            int deleteId = sc.nextInt();
                            sc.nextLine();
                            patientui.deletePatient(deleteId, false);
                            break;

                        case 5:
                            for (DoctorDTO doctor : doctorUI.getAllDoctors()) {
                                System.out.println(doctor);
                            }
                            break;

                        case 6:
                            System.out.print(Constants.PATIENT_ID);
                            int patientId = sc.nextInt();
                            sc.nextLine();
                            for (MedicalRecordDTO medicalRecord : medicalRecordUI.getAllByIdMedicalRecords(patientId)) {
                                medicalRecordUI.printMedicalRecord(medicalRecord);
                            }
                            break;

                        case 7:
                            int newRecordId = medicalRecordUI.addMedicalRecord(
                                    medicalRecordUI.readMedicalRecord(0));
                            System.out.println(Constants.MEDICAL_RECORD_ADDED + newRecordId);
                            break;

                        case 8:
                            System.out.print(Constants.MEDICAL_RECORD_ID);
                            int updateRecordId = sc.nextInt();
                            sc.nextLine();
                            medicalRecordUI.updateMedicalRecord(
                                    medicalRecordUI.readMedicalRecord(updateRecordId));
                            System.out.println(Constants.MEDICAL_RECORD_UPDATED);
                            break;

                        case 9:
                            System.out.print(Constants.MEDICAL_RECORD_ID);
                            int deleteRecordId = sc.nextInt();
                            sc.nextLine();
                            medicalRecordUI.deleteMedicalRecord(deleteRecordId);
                            System.out.println(Constants.MEDICAL_RECORD_DELETED);
                            break;

                        case 10:
                            System.out.print(Constants.PATIENT_ID);
                            int restoreId = sc.nextInt();
                            sc.nextLine();
                            patientui.restorePatient(restoreId);
                            break;

                        case 11:
                            System.out.println(Constants.EXITING);
                            break;

                        default:
                            System.out.println(Constants.INVALID_OPTION);
                            break;
                    }

                } while (option != 11);

            } else {
                System.out.println(Constants.LOGIN_FAILED);
            }
        } catch (DuplicatedUsernameException e) {
            System.out.println(e.getMessage());
        } catch (AppException e) {
            System.out.println(e.getMessage());
        }
    }

}
