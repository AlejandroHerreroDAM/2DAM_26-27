package domain.service;

import dao.model.Doctor;
import dao.interfaces.DoctorRepository;
import domain.mappers.DoctorDTOMapper;
import domain.model.DoctorDTO;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorDTOMapper doctorDTOMapper;

    @Inject
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