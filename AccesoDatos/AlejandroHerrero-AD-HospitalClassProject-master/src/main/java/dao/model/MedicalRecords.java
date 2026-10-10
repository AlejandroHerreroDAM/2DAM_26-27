package dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MedicalRecords {
    private int id;
    private int patientId;
    private int doctorId;
    private String diagnosis;
    private LocalDate addmisionDate;
    private List<PrescribedMed> medications;


}
