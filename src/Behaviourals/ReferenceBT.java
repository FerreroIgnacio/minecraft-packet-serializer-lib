package Behaviourals;

import Serialization.PacketField;

import java.util.List;
import java.util.Map;

public class ReferenceBT extends  AbstractBehavioural{
    private final String name;
    private final AbstractBehavioural type;

    public ReferenceBT(String name, AbstractBehavioural type) {
        super();
        this.name = name;
        this.type = type;
    }

    @Override
    public List<PacketField> asPacketFields() {
        return List.of();
    }

    @Override
    public String toString() {
        return name;
    }
}
