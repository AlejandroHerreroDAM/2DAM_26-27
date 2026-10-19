package org.hospitalspringclient.domain.mappers;


import org.hospitalspringclient.dao.model.Patient;
import org.hospitalspringclient.domain.model.PatientDTO;

public class PatientDTOMapper {
    public Patient dtoToEntity(PatientDTO patientDTO) {
        return new Patient(
                patientDTO.getId(),
                patientDTO.getName(),
                patientDTO.getBirthDate(),
                patientDTO.getPhone(),
                null
        );
    }

    public PatientDTO entityToDto(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getName(),
                patient.getBirthDate(),
                patient.getPhone(),
                0,
                null,
                null);
    }

}
