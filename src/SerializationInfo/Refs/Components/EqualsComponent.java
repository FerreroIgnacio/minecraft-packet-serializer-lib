package SerializationInfo.Refs.Components;

import java.util.Objects;

public class EqualsComponent implements Condition {
    private final String name;
    private final Integer intValue;
    private final String stringValue;
    private Boolean negated = false;
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
            return "(" + (negated ? "!" : "") + name + ".equals(" + stringValue + "))";
        return "(" + name + (negated ? " != " : " == ")  + intValue + ")";
    }

    @Override
    public Condition negate() {
        this.negated = !negated;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EqualsComponent that = (EqualsComponent) o;
        return Objects.equals(name, that.name) && Objects.equals(intValue, that.intValue) && Objects.equals(stringValue, that.stringValue) && Objects.equals(negated, that.negated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, intValue, stringValue, negated);
    }
}
