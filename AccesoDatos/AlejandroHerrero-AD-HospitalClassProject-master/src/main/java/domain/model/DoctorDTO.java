package domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO {
    private int id;
    private String name;
    private String specialization;
    private String phone;
    private String userName;
    private String password;
}
