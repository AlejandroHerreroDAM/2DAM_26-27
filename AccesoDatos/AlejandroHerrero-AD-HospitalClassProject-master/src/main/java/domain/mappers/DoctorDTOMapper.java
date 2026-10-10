package domain.mappers;

import dao.model.Doctor;
import domain.model.DoctorDTO;
import domain.model.PatientDTO;

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
