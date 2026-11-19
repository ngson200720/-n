package vn.edu.englishcenter.model;

public class ComboItem {
    private final String value;
    private final String label;

    public ComboItem(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() { return value; }
    public String getLabel() { return label; }

    @Override
    public String toString() {
        return value + " - " + label;
    }
}
