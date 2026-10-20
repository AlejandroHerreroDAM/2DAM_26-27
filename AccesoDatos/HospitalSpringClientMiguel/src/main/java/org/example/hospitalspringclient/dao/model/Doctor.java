package org.example.hospitalspringclient.dao.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private String phone;
}

