package ui;

import domain.model.MedicalRecordDTO;
import domain.service.MedicalRecordsService;
import common.Constants;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Scanner;

public class MedicalRecordUI {
    private final MedicalRecordsService medicalRecordsService;

    @Inject
    public MedicalRecordUI(MedicalRecordsService medicalRecordsService) {
        this.medicalRecordsService = medicalRecordsService;
    }

    public List<MedicalRecordDTO> getAllByIdMedicalRecords(int patientId){
        return medicalRecordsService.getAllByIdMedicalRecords(patientId);
    }

    public int addMedicalRecord(MedicalRecordDTO medicalRecordDTO){
        return medicalRecordsService.addMedicalRecord(medicalRecordDTO);
    }

    public void updateMedicalRecord(MedicalRecordDTO medicalRecordDTO){
        medicalRecordsService.updateMedicalRecord(medicalRecordDTO);
    }

    public void deleteMedicalRecord(int medicalRecordId){
        medicalRecordsService.deleteMedicalRecord(medicalRecordId);
    }

    public MedicalRecordDTO readMedicalRecord(int id) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(Constants.PATIENT_ID);
        int patientId = scanner.nextInt();
        scanner.nextLine();
        System.out.print(Constants.DOCTOR_ID);
        int doctorId = scanner.nextInt();
        scanner.nextLine();
        System.out.print(Constants.DIAGNOSIS);
        String diagnosis = scanner.nextLine();
        System.out.print(Constants.ADMISSION_DATE);
        String admissionDate = scanner.nextLine();
        return new MedicalRecordDTO(id, diagnosis, admissionDate, patientId, doctorId);
    }

    public void printMedicalRecord(MedicalRecordDTO medicalRecord) {
        System.out.println(medicalRecord);
    }

}
