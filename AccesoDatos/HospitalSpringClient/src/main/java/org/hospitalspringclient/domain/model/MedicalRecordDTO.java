package org.hospitalspringclient.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
