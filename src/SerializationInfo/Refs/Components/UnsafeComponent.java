package SerializationInfo.Refs.Components;

import java.awt.*;
import java.util.Objects;

public class UnsafeComponent implements RefComponent {
    String name;

    public UnsafeComponent(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UnsafeComponent that = (UnsafeComponent) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
