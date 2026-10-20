package org.example.hospitalspringclient.dao.repositories.JDBC;

import org.example.hospitalspringclient.dao.repositories.MedicationsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JDBCMedicationsRepository implements MedicationsRepository {

    @Override
    public List<String> getAll() {
        return List.of();
    }
}
