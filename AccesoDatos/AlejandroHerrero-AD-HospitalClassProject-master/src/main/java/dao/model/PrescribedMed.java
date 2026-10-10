package dao.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescribedMed {
    private int prescriptionId;
    private int recordId;
    private String medicationName;
    private String dosage;
}
