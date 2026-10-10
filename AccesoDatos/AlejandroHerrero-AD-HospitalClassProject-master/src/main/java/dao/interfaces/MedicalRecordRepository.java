package dao.interfaces;

import dao.model.MedicalRecords;
import dao.model.Patient;

import java.util.List;

public interface MedicalRecordRepository {

    List<MedicalRecords> getAllById(int PatientId);

    int add(MedicalRecords medicalRecords);

    void update(MedicalRecords medicalRecords);

    void delete(int idDelete);
}
