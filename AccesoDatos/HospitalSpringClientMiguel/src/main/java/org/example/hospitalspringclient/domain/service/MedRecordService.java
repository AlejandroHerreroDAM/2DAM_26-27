package org.example.hospitalspringclient.domain.service;
import org.example.hospitalspringclient.dao.model.MedicalRecord;
import org.example.hospitalspringclient.dao.repositories.MedRecordRepository;
import org.example.hospitalspringclient.domain.mappers.MedicalRecordDTOMapper;
import org.example.hospitalspringclient.domain.model.MedicalRecordDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class MedRecordService {
    private final MedRecordRepository medRecordRepository;
    private final MedicalRecordDTOMapper medicalRecordDTOMapper;

    public MedRecordService(MedRecordRepository medRecordRepository, MedicalRecordDTOMapper medicalRecordDTOMapper) {
        this.medRecordRepository = medRecordRepository;
        this.medicalRecordDTOMapper = medicalRecordDTOMapper;
    }

    public List<MedicalRecordDTO> getAllByPatientId(int patientId) {
        List<MedicalRecord> medicalRecords = medRecordRepository.getAllById(patientId);
        List<MedicalRecordDTO> medicalRecordDTOS = new ArrayList<>();
        for(MedicalRecord medicalRecord : medicalRecords){
            MedicalRecordDTO medicalRecordDTO = medicalRecordDTOMapper.entityToDto(medicalRecord);
            medicalRecordDTOS.add(medicalRecordDTO);
        }
        return medicalRecordDTOS;
    }

    public int addMedRecord(MedicalRecordDTO medRecord) {
        return medRecordRepository.add(medicalRecordDTOMapper.dtoToEntity(medRecord));
    }

    public void updateMedRecord(MedicalRecordDTO medRecord) {
        medRecordRepository.update(medicalRecordDTOMapper.dtoToEntity(medRecord));
    }

    public void deleteMedRecord(int idDelete,boolean confirmation) {
        medRecordRepository.delete(idDelete,confirmation);
    }

}
