package org.hospitalspringclient.domain.service;

import org.hospitalspringclient.dao.repositories.PrescribedMedRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationsService {

    PrescribedMedRepository jdbcMedicationsRepository;

    public List<String> getAllMedications(){
        return jdbcMedicationsRepository.getAll();
    }
}
