package Behaviourals;

import Serialization.PacketField;
import SerializationInfo.Refs.DeserializerRef;

import java.util.List;

public class BufferBT extends AbstractBehavioural {

    private final Integer fixedCount;
    private final String fieldName;
    public BufferBT(String fieldPath) {
        super();
        this.fieldName = fieldPath;
        this.fixedCount = null;
    }
    public BufferBT(Integer fixedCount) {
        super();
        this.fixedCount = fixedCount;
        this.fieldName = null;
    }

    @Override
    public List<PacketField> asPacketFields() {
      //  AbstractBehavioural behavioural = resolvePath()
       // DeserializerRef readBuffer = new DeserializerRef();
        return List.of();
    }
}
