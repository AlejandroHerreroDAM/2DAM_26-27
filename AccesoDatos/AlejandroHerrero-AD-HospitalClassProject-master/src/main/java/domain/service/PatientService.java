package domain.service;

import dao.interfaces.PatientRepository;
import dao.model.Patient;
import dao.repositories.JDBCPatientRepository;
import domain.mappers.PatientDTOMapper;
import domain.model.PatientDTO;
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

    public int addPatient(PatientDTO patient){
        validatePatient(patient);
        return patientRepository.add(patientMapper.dtoToEntity(patient));
    }

    public void updatePatient(PatientDTO patient){
        validatePatient(patient);
        patientRepository.update(patientMapper.dtoToEntity(patient));
    }

    public void deletePatient(int id ){
        patientRepository.delete(id);
    }

    private void validatePatient(PatientDTO patient) {
        if (patient == null) {
            throw new IllegalArgumentException("El paciente no puede ser null");
        }
        if (patient.getName() == null || patient.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del paciente es obligatorio");
        }
        if (patient.getBirthDate() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento del paciente es obligatoria");
        }
    }
}
