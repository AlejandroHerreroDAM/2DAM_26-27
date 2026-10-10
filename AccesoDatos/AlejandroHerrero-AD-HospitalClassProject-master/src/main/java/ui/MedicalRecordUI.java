package ui;

import domain.model.MedicalRecordDTO;
import domain.service.MedicalRecordsService;
import jakarta.inject.Inject;

import java.util.List;

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

}
