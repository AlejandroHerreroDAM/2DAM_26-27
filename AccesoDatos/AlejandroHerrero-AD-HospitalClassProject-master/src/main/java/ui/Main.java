package ui;

import domain.exceptions.AppException;
import domain.model.CredentialDTO;
import domain.model.DoctorDTO;
import domain.model.PatientDTO;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import java.time.LocalDate;
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

            System.out.println("""
                    Hospital CRUD application
                    Please login to continue
                    Username: """);
            System.out.println("Password: ");

            Scanner sc = new Scanner(System.in);
            String username = sc.nextLine();
            String password = sc.nextLine();
            if (credentialUI.login(new CredentialDTO(username, password))) {
                System.out.println("Login successful!");
                int option;

                do {
                    System.out.println("""
                            
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
                            """);

                    System.out.print("Enter your choice: ");
                    option = sc.nextInt();
                    sc.nextLine();

                    switch (option) {
                        case 1:
                            for (PatientDTO patient : patientui.getAllPatients()) {
                                System.out.println(patient);
                            }
                            break;

                        case 2:
                            PatientDTO newPatient = readPatient(sc, 0);
                            int newId = patientui.addPatient(newPatient);
                            System.out.println("Patient added with id: " + newId);
                            break;

                        case 3:
                            System.out.print("Patient id: ");
                            int updateId = sc.nextInt();
                            sc.nextLine();
                            patientui.updatePatient(readPatient(sc, updateId));
                            System.out.println("Patient updated.");
                            break;

                        case 4:
                            System.out.print("Patient id: ");
                            int deleteId = sc.nextInt();
                            sc.nextLine();
                            patientui.deletePatient(deleteId);
                            System.out.println("Patient deleted.");
                            break;

                        case 5:
                            for (DoctorDTO doctor : doctorUI.getAllDoctors()) {
                                System.out.println(doctor);
                            }
                            break;

                        case 6:
                            int patientId = sc.nextInt();
                            sc.nextLine();
                            medicalRecordUI.getAllByIdMedicalRecords(patientId);
                            break;

                        case 7:
                            medicalRecordUI.addMedicalRecord(null);
                            break;

                        case 8:
                            medicalRecordUI.updateMedicalRecord(null);
                            break;

                        case 9:
                            medicalRecordUI.deleteMedicalRecord(0);
                            break;

                        case 10:
                            System.out.println("Exiting...");
                            break;

                        default:
                            System.out.println("Invalid option!");
                            break;
                    }

                } while (option != 10);

            } else {
                System.out.println("Login failed!");
            }
        } catch (AppException e) {
            
        }
    }

    private static PatientDTO readPatient(Scanner sc, int id) {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Date of birth (yyyy-MM-dd): ");
        String date = sc.nextLine();
        LocalDate birthDate = LocalDate.parse(date);
        System.out.print("Phone: ");
        String phone = sc.nextLine();
        return new PatientDTO(id, name, birthDate, phone, 0, null, null);
    }

}
