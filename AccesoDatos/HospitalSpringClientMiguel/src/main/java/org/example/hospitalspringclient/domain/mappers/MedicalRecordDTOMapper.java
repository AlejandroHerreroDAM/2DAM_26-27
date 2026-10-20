package org.example.hospitalspringclient.domain.mappers;




import org.example.hospitalspringclient.dao.model.MedicalRecord;
import org.example.hospitalspringclient.domain.model.MedicalRecordDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MedicalRecordDTOMapper {

    public MedicalRecord dtoToEntity(MedicalRecordDTO medicalRecordDTO) {
        LocalDate date = null;
        if (medicalRecordDTO.getDate() != null) {
            date = LocalDate.parse(medicalRecordDTO.getDate());
        }
        return new MedicalRecord(medicalRecordDTO.getId(), medicalRecordDTO.getIdPatient(), medicalRecordDTO.getIdDoctor(), medicalRecordDTO.getDescription(), date, medicalRecordDTO.getMedications());
    }

    public MedicalRecordDTO entityToDto(MedicalRecord medicalRecord) {
        String date = null;
        if (medicalRecord.getAdmission_date() != null) {
            date = medicalRecord.getAdmission_date().toString();
        }
        return new MedicalRecordDTO(medicalRecord.getId(), medicalRecord.getDiagnosis(),date, medicalRecord.getPatient_id(), medicalRecord.getDoctor_id(), medicalRecord.getMedications());
    }
}
