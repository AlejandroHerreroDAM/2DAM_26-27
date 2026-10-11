package domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicalRecordDTO {

        private int id;
        private String description;
        private String date;
        private int idPatient;
        private int idDoctor;

}
