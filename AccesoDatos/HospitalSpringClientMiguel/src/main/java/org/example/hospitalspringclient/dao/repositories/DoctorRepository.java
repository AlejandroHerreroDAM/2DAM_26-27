package org.example.hospitalspringclient.dao.repositories;

import org.example.hospitalspringclient.dao.model.Doctor;

import java.util.List;

public interface DoctorRepository {

    List<Doctor> getAll();

}
