package org.hospitalspringclient.ui;

import org.hospitalspringclient.domain.model.MedicalRecordDTO;
import org.hospitalspringclient.domain.service.MedicalRecordsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class RestMedicalRecord {
    private final MedicalRecordsService medicalRecordService;

    public RestMedicalRecord(MedicalRecordsService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }


    @GetMapping("/patients/{patientId}/medRecords")
    public List<MedicalRecordDTO> getMedicalRecords(@PathVariable int patientId) {
        return medicalRecordService.getAllByIdMedicalRecords(patientId);
    }

    @PostMapping("/patients/medRecords")
    public int addMedRecord(@RequestBody MedicalRecordDTO medicalRecordUI) {
        return medicalRecordService.addMedicalRecord(medicalRecordUI);

    }

    @DeleteMapping("/patients/medRecords/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedRecord(@PathVariable int id) {
        medicalRecordService.deleteMedicalRecord(id);
    }

    @PutMapping("/patients/medRecords")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMedRecord(@RequestBody MedicalRecordDTO medicalRecordUI) {
        medicalRecordService.updateMedicalRecord(medicalRecordUI);
    }

}
