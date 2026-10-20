package org.example.hospitalspringclient.ui;


import org.example.hospitalspringclient.domain.model.MedicalRecordDTO;
import org.example.hospitalspringclient.domain.service.MedRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class RestMedicalRecord {
    private final MedRecordService medicalRecordService;

    public RestMedicalRecord(MedRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }


    @GetMapping("/patients/{patientId}/medRecords")
    public List<MedicalRecordDTO> getMedicalRecords(@PathVariable int patientId) {
        return medicalRecordService.getAllByPatientId(patientId);
    }

    @PostMapping("/patients/medRecords")
    public int addMedRecord(@RequestBody MedicalRecordDTO medicalRecordDTO) {
        return medicalRecordService.addMedRecord(medicalRecordDTO);

    }

    @DeleteMapping("/patients/medRecords/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedRecord(@PathVariable int id) {
        medicalRecordService.deleteMedRecord(id,true);
    }

    @PutMapping("/patients/medRecords")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMedRecord(@RequestBody MedicalRecordDTO medicalRecordDTO) {
        medicalRecordService.updateMedRecord(medicalRecordDTO);
    }

}
