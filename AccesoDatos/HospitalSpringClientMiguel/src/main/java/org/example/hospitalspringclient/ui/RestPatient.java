package org.example.hospitalspringclient.ui;


import org.example.hospitalspringclient.domain.model.PatientDTO;
import org.example.hospitalspringclient.domain.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class RestPatient {
    private final PatientService patientService;
    public RestPatient(PatientService patientService) {
        this.patientService = patientService;
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/patients")
    public List<PatientDTO> getPatients () {
        return patientService.getAllPatients();
    }

   @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping("/patients")
    public int addPatient (@RequestBody PatientDTO patientDTO) {
        return patientService.addPatient(patientDTO);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PutMapping("/patients")
    public void updatePatient (@RequestBody PatientDTO patientDTO) {
        patientService.updatePatient(patientDTO);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @DeleteMapping("/patients/{patientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable int patientId, @RequestParam(required = false) boolean confirm) {
        patientService.deletePatient(patientId,confirm);
    }
}