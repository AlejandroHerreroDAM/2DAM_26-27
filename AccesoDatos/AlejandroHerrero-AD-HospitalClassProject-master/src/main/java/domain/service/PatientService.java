package domain.service;

import dao.interfaces.PatientRepository;
import dao.model.Patient;
import dao.repositories.JDBCPatientRepository;
import common.Constants;
import domain.mappers.PatientDTOMapper;
import domain.model.PatientDTO;
import domain.model.CredentialDTO;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientDTOMapper patientMapper;

    @Inject
    public PatientService(JDBCPatientRepository patientRepository, PatientDTOMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public List<PatientDTO> getAllPatients(){
        List<Patient> patients = patientRepository.getAll();
        List<PatientDTO> patientsDTO = new ArrayList<>();
        for (Patient patient : patients) {
            patientsDTO.add(patientMapper.entityToDto(patient));
        }
        return patientsDTO;
    }

    public int addPatient(PatientDTO patient, CredentialDTO credential) {
        validatePatient(patient);
        validateCredential(credential);

        dao.model.UserLogin userLogin = new dao.model.UserLogin();
        userLogin.setUsername(credential.getUsername().trim());
        userLogin.setPassword(credential.getPassword());
        return patientRepository.add(patientMapper.dtoToEntity(patient), userLogin);
    }

    public void updatePatient(PatientDTO patient){
        validatePatient(patient);
        patientRepository.update(patientMapper.dtoToEntity(patient));
    }

    public void deletePatient(int id, boolean b){
        patientRepository.delete(id,b);
    }

    private void validatePatient(PatientDTO patient) {
        if (patient == null) {
            throw new IllegalArgumentException(Constants.NULL_PATIENT);
        }
        if (patient.getName() == null || patient.getName().isBlank()) {
            throw new IllegalArgumentException(Constants.REQUIRED_PATIENT_NAME);
        }
        if (patient.getBirthDate() == null) {
            throw new IllegalArgumentException(Constants.REQUIRED_BIRTH_DATE);
        }
    }

    private void validateCredential(CredentialDTO credential) {
        if (credential == null
                || credential.getUsername() == null
                || credential.getUsername().isBlank()
                || credential.getPassword() == null
                || credential.getPassword().isBlank()) {
            throw new IllegalArgumentException(Constants.REQUIRED_CREDENTIALS);
        }
    }
}
