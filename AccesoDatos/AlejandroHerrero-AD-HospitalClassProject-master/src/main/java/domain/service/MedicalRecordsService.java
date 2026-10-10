package domain.service;

import dao.model.MedicalRecords;
import dao.repositories.JDBCMedicalRecordsRepository;
import domain.mappers.MedicalRecordDTOMapper;
import domain.mappers.PatientDTOMapper;
import domain.model.MedicalRecordDTO;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordsService {

    private final JDBCMedicalRecordsRepository medicalRecordsRepository;
    private final MedicalRecordDTOMapper medicalRecordDTOMapper;

    @Inject
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
