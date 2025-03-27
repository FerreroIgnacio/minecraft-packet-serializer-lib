package SerializationInfo.Refs.Components;

public class EqualsComponent implements Condition {
    private final String name;
    private final Integer intValue;
    private final String stringValue;
    public EqualsComponent(String name, String value) {
        this.name = name;
        this.stringValue = value;
        this.intValue = null;
    }

    public EqualsComponent(String name, Integer intValue) {
        this.name = name;
        this.intValue = intValue;
        this.stringValue = null;
    }

    @Override
    public String toString() {
        if(stringValue != null)
            return "(" + name + ".equals(" + stringValue + ")";
        return "(" + name + " == " + intValue + ")";
    }
}
