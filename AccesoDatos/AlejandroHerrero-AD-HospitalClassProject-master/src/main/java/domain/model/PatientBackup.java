package domain.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PatientBackup {
    private final Map<String, List<BackupRow>> tables = new LinkedHashMap<>();

    public Map<String, List<BackupRow>> getTables() {
        return tables;
    }
}
