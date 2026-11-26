package domain.model;

public class BackupValue {
    private final String type;
    private final String value;

    public BackupValue(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
