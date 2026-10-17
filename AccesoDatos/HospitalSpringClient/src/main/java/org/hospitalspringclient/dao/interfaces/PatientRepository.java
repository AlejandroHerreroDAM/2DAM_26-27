package org.hospitalspringclient.dao.interfaces;



import org.hospitalspringclient.dao.model.Patient;
import org.hospitalspringclient.dao.model.UserLogin;

import java.util.List;

public interface PatientRepository {

    List<Patient> getAll();

    int add(Patient patient, UserLogin userLogin);

    void update(Patient patientDatabase);

    void delete(int idDelete, boolean confirm);
}
