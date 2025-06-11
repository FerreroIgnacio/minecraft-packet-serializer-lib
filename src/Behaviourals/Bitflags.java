package Behaviourals;

import Serialization.PacketField;

import java.util.LinkedHashMap;
import java.util.List;

public class Bitflags extends AbstractBehavioural{
    private final ClassBT type;
    public Bitflags(LinkedHashMap<String, AbstractBehavioural> children, ClassBT type) {
        super(children);
        this.type = type;
    }

    @Override
    public List<PacketField> asPacketFields() {
        return List.of(new PacketField(getName(), type.asPacketFields().getFirst().getSerializationInfo(), List.of(this)));
    }
}
