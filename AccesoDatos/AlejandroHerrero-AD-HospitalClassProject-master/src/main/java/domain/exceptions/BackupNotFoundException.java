package domain.exceptions;

public class BackupNotFoundException extends AppException {
    public BackupNotFoundException(int patientId) {
        super("No backup exists for patient " + patientId);
    }
}
