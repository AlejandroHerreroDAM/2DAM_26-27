package org.hospitalspringclient.dao.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
    private int id;
    private String username;
    private String password;
    private int patientId;
    private int doctorId;
}
