package dao.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

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
