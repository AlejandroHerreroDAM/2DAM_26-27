package org.example.hospitalspringclient.domain.mappers;


import org.example.hospitalspringclient.dao.model.Doctor;
import org.example.hospitalspringclient.domain.model.DoctorDTO;
import org.springframework.stereotype.Component;

@Component
public class DoctorDTOMapper {
    public Doctor dtoToEntity(DoctorDTO doctorDTO) {
        return new Doctor(doctorDTO.getId(),doctorDTO.getName(),null,null);
    }

    public DoctorDTO entityToDto(Doctor doctor) {
        return new DoctorDTO(doctor.getId(),doctor.getName());
    }

}
