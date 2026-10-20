package org.example.hospitalspringclient.dao.repositories;

import org.example.hospitalspringclient.dao.model.Patient;

import java.util.List;

public interface PatientRepository {

    List<Patient> getAll();

    int add(Patient patient);

    void update(Patient patient);

    void delete(int idDelete, boolean confirmation);

}
