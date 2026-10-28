package org.hospitalspringclient.dao.repositories;

import org.hospitalspringclient.dao.interfaces.MedicationsRepository;

import java.util.List;

public class PrescribedMedRepository implements MedicationsRepository {
    @Override
    public List<String> getAll() {
        return List.of("Jarabe de palo","Fentanilo","Paracetamol");
    }
}
