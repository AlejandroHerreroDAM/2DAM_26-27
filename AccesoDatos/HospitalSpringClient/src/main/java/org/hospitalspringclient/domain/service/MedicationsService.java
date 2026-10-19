package org.hospitalspringclient.domain.service;

import org.hospitalspringclient.dao.repositories.JDBCMedicationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationsService {

    JDBCMedicationsRepository  jdbcMedicationsRepository;

    public List<String> getAllMedications(){
        return jdbcMedicationsRepository.getAll();
    }
}
