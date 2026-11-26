package domain.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class BackupRow {
    private final Map<String, BackupValue> values = new LinkedHashMap<>();

    public Map<String, BackupValue> getValues() {
        return values;
    }

    public void put(String column, BackupValue value) {
        values.put(column, value);
    }
}
