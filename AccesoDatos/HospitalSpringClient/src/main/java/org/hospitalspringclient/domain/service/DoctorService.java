package org.hospitalspringclient.domain.service;

import org.hospitalspringclient.dao.interfaces.DoctorRepository;
import org.hospitalspringclient.dao.model.Doctor;
import org.hospitalspringclient.domain.mappers.DoctorDTOMapper;
import org.hospitalspringclient.domain.model.DoctorDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorDTOMapper doctorDTOMapper;

    public DoctorService(DoctorRepository doctorRepository, DoctorDTOMapper doctorDTOMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorDTOMapper = doctorDTOMapper;
    }

    public List<DoctorDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.getAll();
        List<DoctorDTO> doctorsDTO = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorsDTO.add(doctorDTOMapper.entityToDto(doctor));
        }
        return doctorsDTO;
    }

}