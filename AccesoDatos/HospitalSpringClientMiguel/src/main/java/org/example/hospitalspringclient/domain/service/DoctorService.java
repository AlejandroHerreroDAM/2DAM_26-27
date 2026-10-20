package org.example.hospitalspringclient.domain.service;
import org.example.hospitalspringclient.dao.model.Doctor;
import org.example.hospitalspringclient.dao.repositories.DoctorRepository;
import org.example.hospitalspringclient.domain.mappers.DoctorDTOMapper;
import org.example.hospitalspringclient.domain.model.DoctorDTO;
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

    public List<DoctorDTO> getAll() {
        List<Doctor> doctors = doctorRepository.getAll();
        List<DoctorDTO> doctorDTOS = new ArrayList<>();
        for (Doctor doctor : doctors) {
            DoctorDTO doctorDTO = doctorDTOMapper.entityToDto(doctor);
            doctorDTOS.add(doctorDTO);
        }
        return doctorDTOS;
    }
}
