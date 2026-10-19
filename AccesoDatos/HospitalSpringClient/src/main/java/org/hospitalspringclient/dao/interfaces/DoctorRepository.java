package org.hospitalspringclient.dao.interfaces;

import org.hospitalspringclient.dao.model.Doctor;

import java.util.List;

public interface DoctorRepository {

    List<Doctor> getAll();
}
