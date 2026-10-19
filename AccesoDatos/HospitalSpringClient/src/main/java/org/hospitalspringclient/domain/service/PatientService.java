package org.hospitalspringclient.domain.service;

import org.hospitalspringclient.common.Constants;
import org.hospitalspringclient.dao.interfaces.PatientRepository;
import org.hospitalspringclient.dao.model.Patient;
import org.hospitalspringclient.dao.model.UserLogin;
import org.hospitalspringclient.dao.repositories.SpringPatientRepository;
import org.hospitalspringclient.domain.mappers.PatientDTOMapper;
import org.hospitalspringclient.domain.model.CredentialDTO;
import org.hospitalspringclient.domain.model.PatientDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientDTOMapper patientMapper;


    public PatientService(SpringPatientRepository patientRepository, PatientDTOMapper patientMapper) {
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

        UserLogin userLogin = new UserLogin();
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
