package dao.repositories;

import dao.interfaces.MedicalRecordRepository;
import dao.mapper.medicalRecordsQueryMapper;
import dao.model.MedicalRecords;
import dao.repositories.common.SQLQueries;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCMedicalRecordsRepository implements MedicalRecordRepository {

    private final DBConnection db;
    private final medicalRecordsQueryMapper medicalRecordMapper;

    @Inject
    public JDBCMedicalRecordsRepository(DBConnection db, medicalRecordsQueryMapper medicalRecordMapper) {
        this.db = db;
        this.medicalRecordMapper = medicalRecordMapper;
    }


    @Override
    public List<MedicalRecords> getAllById(int PatientId) {
        List<MedicalRecords> medicalRecords = new ArrayList<>();
        try (Connection con = db.getConnection(); Statement statement = con.createStatement()){
            ResultSet rs = statement.executeQuery(SQLQueries.SELECT_AllMedicalRecordsByPatientId_QUERY + PatientId);
            medicalRecords = medicalRecordMapper;
        } catch (SQLException ex){
            Logger.getLogger(JDBCMedicalRecordsRepository.class.getName()).log(Level.SEVERE, null, ex);

        }
        return medicalRecords;
    }

    @Override
    public int add(MedicalRecords medicalRecords) {
        return medicalRecords.getId();
    }

    @Override
    public void update(MedicalRecords medicalRecords) {

    }

    @Override
    public void delete(int idDelete) {

    }
}
