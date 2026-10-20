package org.example.hospitalspringclient.domain.service;
import org.example.hospitalspringclient.dao.model.Patient;
import org.example.hospitalspringclient.dao.repositories.PatientRepository;
import org.example.hospitalspringclient.domain.mappers.PatientDTOMapper;
import org.example.hospitalspringclient.domain.model.PatientDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientDTOMapper patientDTOMapper;


    public PatientService(PatientRepository patientRepository, PatientDTOMapper patientDTOMapper) {
        this.patientRepository = patientRepository;
        this.patientDTOMapper = patientDTOMapper;
    }

    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.getAll();
        List<PatientDTO> patientsDTOS = new ArrayList<>();
        for (Patient patient : patients) {
            PatientDTO patientsDTO = patientDTOMapper.entityToDto(patient);
            patientsDTOS.add(patientsDTO);
        }
        return patientsDTOS;
    }

    public int addPatient(PatientDTO patientDTO) {
        return patientRepository.add(patientDTOMapper.dtoToEntity(patientDTO));
    }

    public void updatePatient(PatientDTO patientDTO) {
        patientRepository.update(patientDTOMapper.dtoToEntity(patientDTO));
    }

    public void deletePatient(int idDelete, boolean confirmation) {
        patientRepository.delete(idDelete,confirmation);
    }


}
