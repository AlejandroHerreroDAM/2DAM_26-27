package org.hospitalspringclient.domain.mappers;


import org.hospitalspringclient.dao.model.MedicalRecords;
import org.hospitalspringclient.domain.model.MedicalRecordDTO;

import java.time.LocalDate;

public class MedicalRecordDTOMapper {

    public MedicalRecords dtoToEntity(MedicalRecordDTO medicalRecordDTO) {
        return new MedicalRecords(
                medicalRecordDTO.getId(),
                medicalRecordDTO.getIdPatient(),
                medicalRecordDTO.getIdDoctor(),
                medicalRecordDTO.getDescription(),
                LocalDate.parse(medicalRecordDTO.getDate())
        );
    }

    public MedicalRecordDTO entityToDto(MedicalRecords medicalRecords) {
        return new MedicalRecordDTO(
                medicalRecords.getId(),
                medicalRecords.getDiagnosis(),
                medicalRecords.getAddmisionDate().toString(),
                medicalRecords.getPatientId(),
                medicalRecords.getDoctorId()
        );
    }
}
