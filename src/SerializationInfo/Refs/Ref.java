package SerializationInfo.Refs;

import SerializationInfo.Refs.Components.RefComponent;

public abstract class Ref {
    protected final RefComponent component;

    public Ref(RefComponent component) {
        this.component = component;
    }
}
