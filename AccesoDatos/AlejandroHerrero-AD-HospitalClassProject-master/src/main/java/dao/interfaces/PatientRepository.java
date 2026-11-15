package dao.interfaces;

import dao.model.Patient;
import dao.model.UserLogin;
import domain.model.PatientBackup;

import java.util.List;

public interface PatientRepository {

    List<Patient> getAll();

    int add(Patient patient, UserLogin userLogin);

    void update(Patient patientDatabase);

    void delete(int idDelete, boolean confirm);

    PatientBackup createBackup(int patientId);

    void restore(PatientBackup backup);
}
