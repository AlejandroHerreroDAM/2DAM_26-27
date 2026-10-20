package org.example.hospitalspringclient.domain.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDTO {

        private int id;
        private String name;
        private LocalDate birthDate;
        private String phone;
        private int paid;
        private String userName;
        private String password;


    public PatientDTO(int id, String name, LocalDate birthDate, String phone, int paid) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.paid = paid;
    }

    public PatientDTO(int id, String name, LocalDate birthDate, String phone, int paid, String userName, String password) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.paid = paid;
        this.userName = userName;
        this.password = password;
    }

    public PatientDTO( String name, LocalDate birthDate, String phone, int paid, String userName, String password){
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.paid = paid;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "{id= "+id+", name= "+name+", birthDate= " + birthDate +", phone= " + phone +", paid= " + paid + "}";
    }
}

