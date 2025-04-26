package SerializationInfo.Refs;

import SerializationInfo.Refs.Components.RefComponent;

import java.util.Objects;

public abstract class Ref {
    protected final RefComponent component;

    public Ref(RefComponent component) {
        this.component = component;
    }

    @Override
    public String toString() {
        return component.toString();
    }

    public RefComponent getComponent() {
        return component;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ref ref = (Ref) o;
        return Objects.equals(component, ref.component);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(component);
    }
}
