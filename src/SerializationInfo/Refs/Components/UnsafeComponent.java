package SerializationInfo.Refs.Components;

import java.awt.*;

public class UnsafeComponent implements RefComponent {
    String name;

    public UnsafeComponent(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
