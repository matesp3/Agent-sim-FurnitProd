package gui.models;

public class StatsModel {
    private String description;
    private String value;
    private String unit;

    public StatsModel(String description, String value, String unit) {
        this.description = description;
        this.value = value;
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
