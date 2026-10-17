package org.hospitalspringclient.domain.mappers;


import org.hospitalspringclient.dao.model.Doctor;
import org.hospitalspringclient.domain.model.DoctorDTO;

public class DoctorDTOMapper {
    public Doctor dtoToEntity(DoctorDTO doctorDTO) {
        return new Doctor(
                doctorDTO.getId(),
                doctorDTO.getName(),
                doctorDTO.getSpecialization(),
                doctorDTO.getPhone()
        );
    }


    public DoctorDTO entityToDto(Doctor doctor) {
        return new DoctorDTO(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getPhone(),
                null,
                null
        );
    }
}
