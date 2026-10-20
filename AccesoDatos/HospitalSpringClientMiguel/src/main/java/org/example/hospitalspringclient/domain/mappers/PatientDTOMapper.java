package org.example.hospitalspringclient.domain.mappers;


import org.example.hospitalspringclient.dao.model.Credential;
import org.example.hospitalspringclient.dao.model.Patient;
import org.example.hospitalspringclient.domain.model.PatientDTO;
import org.springframework.stereotype.Component;

@Component
public class PatientDTOMapper {
    public Patient dtoToEntity(PatientDTO patientDTO) {

        return new Patient(patientDTO.getId(),patientDTO.getName(),patientDTO.getBirthDate(),patientDTO.getPhone(),new Credential(patientDTO.getUserName(),patientDTO.getPassword()));

    }

    public PatientDTO entityToDto(Patient patient1) {
        return new PatientDTO(patient1.getId(),patient1.getName(),
                patient1.getBirthDate(),patient1.getPhone(),0,null,null);
    }

}