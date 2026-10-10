package ui;

import domain.exceptions.AppException;
import domain.exceptions.DatabaseError;
import domain.model.PatientDTO;
import domain.service.PatientService;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
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
            throw new AppException("Critical error with GetAllPatients: " + e.getMessage());
        }
        return patients;
    }

    public int addPatient(PatientDTO patient) {
        int id = 0;
        try {
            id = patientService.addPatient(patient);
        } catch (DatabaseError ex) {
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, null, e);
            throw new AppException("Critical error with GetAllPatients: " + e.getMessage());
        }
        return id;
    }

    public void updatePatient(PatientDTO patient) {
        patientService.updatePatient(patient);
    }

    public void deletePatient(int idPatient) {
        try{
            patientService.deletePatient(idPatient);
        }
    }

}
