package SerializationInfo.Refs.Components;

import java.util.Objects;

public class ObjectFunction extends FunctionComponent {
    private final String name;
    public ObjectFunction(String name, String function, String... args) {
        super(function, args);
        this.name = name;
    }
    public String toString() {
        return name + "." + super.getFunction() + "(" + String.join(", ", super.getArgs()) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectFunction that = (ObjectFunction) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
