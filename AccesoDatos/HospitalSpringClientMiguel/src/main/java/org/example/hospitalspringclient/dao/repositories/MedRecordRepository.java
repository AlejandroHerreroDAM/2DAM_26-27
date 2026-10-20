package org.example.hospitalspringclient.dao.repositories;



import org.example.hospitalspringclient.dao.model.MedicalRecord;

import java.util.List;

public interface MedRecordRepository {

    List<MedicalRecord> getAllById(int patientId);

    int add(MedicalRecord medicalRecord);

    void update(MedicalRecord medicalRecord);

    void delete(int idDelete,boolean  confirmation);

}
