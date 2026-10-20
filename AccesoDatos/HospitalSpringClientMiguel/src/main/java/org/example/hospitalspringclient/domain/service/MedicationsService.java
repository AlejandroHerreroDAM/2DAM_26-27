package org.example.hospitalspringclient.domain.service;

import org.example.hospitalspringclient.dao.repositories.MedicationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationsService {
    private final MedicationsRepository medicationsRepository;

    public MedicationsService(MedicationsRepository medicationsRepository) {
        this.medicationsRepository = medicationsRepository;
    }

    public List<String> getAllMedications() {
        return medicationsRepository.getAll();
    }
}
