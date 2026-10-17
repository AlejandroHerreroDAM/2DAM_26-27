package org.hospitalspringclient.domain.service;

import org.hospitalspringclient.dao.model.MedicalRecords;
import org.hospitalspringclient.dao.repositories.JDBCMedicalRecordsRepository;
import org.hospitalspringclient.domain.mappers.MedicalRecordDTOMapper;
import org.hospitalspringclient.domain.mappers.PatientDTOMapper;
import org.hospitalspringclient.domain.model.MedicalRecordDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicalRecordsService {

    private final JDBCMedicalRecordsRepository medicalRecordsRepository;
    private final MedicalRecordDTOMapper medicalRecordDTOMapper;

    public MedicalRecordsService(JDBCMedicalRecordsRepository medicalRecordsRepository, MedicalRecordDTOMapper medicalRecordDTOMapper, JDBCMedicalRecordsRepository medicalRecordsRepository1, PatientDTOMapper patientDTOMapper) {
        this.medicalRecordsRepository = medicalRecordsRepository;
        this.medicalRecordDTOMapper = medicalRecordDTOMapper;
    }

    public List<MedicalRecordDTO> getAllByIdMedicalRecords(int patientId){
        List<MedicalRecords> medicalRecords = medicalRecordsRepository.getAllById(patientId);
        List<MedicalRecordDTO> medicalRecordDTO = new ArrayList<>();
        for (MedicalRecords medicalRecord : medicalRecords) {
            medicalRecordDTO.add(medicalRecordDTOMapper.entityToDto(medicalRecord));
        }
        return medicalRecordDTO;
    }

    public int addMedicalRecord(MedicalRecordDTO medicalRecord){
        return medicalRecordsRepository.add(medicalRecordDTOMapper.dtoToEntity(medicalRecord));
    }

    public void updateMedicalRecord(MedicalRecordDTO medicalRecord){
        medicalRecordsRepository.update(medicalRecordDTOMapper.dtoToEntity(medicalRecord));
    }

    public void deleteMedicalRecord(int medicalRecordId){
        medicalRecordsRepository.delete(medicalRecordId);
    }

}
