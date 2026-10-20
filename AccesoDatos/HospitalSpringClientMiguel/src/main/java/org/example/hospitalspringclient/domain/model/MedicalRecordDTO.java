package org.example.hospitalspringclient.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<String> medications;

    public MedicalRecordDTO(String description, String date, int idPatient, int idDoctor, List<String> medications) {
        this.description = description;
        this.date = date;
        this.idPatient = idPatient;
        this.idDoctor = idDoctor;
        this.medications = medications;
    }


    public MedicalRecordDTO(int id, String description, String date, List<String> medications) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.medications = medications;
    }

    @Override
    public String toString() {
        return "{id= " + id + ", description= " + description + ", date= " + date + ", idPatient= " + idPatient + ", idDoctor= " + idDoctor + ", medications= " + medications + "}";
    }


}

