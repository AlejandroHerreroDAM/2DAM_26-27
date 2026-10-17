package org.hospitalspringclient.dao.interfaces;



import org.hospitalspringclient.dao.model.MedicalRecords;

import java.util.List;

public interface MedicalRecordRepository {

    List<MedicalRecords> getAllById(int PatientId);

    int add(MedicalRecords medicalRecords);

    void update(MedicalRecords medicalRecords);

    void delete(int idDelete);
}
