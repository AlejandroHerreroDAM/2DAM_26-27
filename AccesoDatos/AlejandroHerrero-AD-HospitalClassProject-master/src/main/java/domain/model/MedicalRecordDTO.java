package domain.model;

import dao.model.PrescribedMed;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicalRecordDTO {

        private int id;
        private String description;
        private String date;
        private int idPatient;
        private int idDoctor;
        private List<PrescribedMed> medications;

}
