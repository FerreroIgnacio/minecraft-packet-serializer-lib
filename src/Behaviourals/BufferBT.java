package Behaviourals;

import Serialization.PacketField;
import SerializationInfo.Refs.DeserializerRef;

import java.util.List;

public class BufferBT extends AbstractBehavioural {

    private final Integer fixedCount;
    private final String fieldName;
    public BufferBT(String fieldName) {
        super();
        this.fieldName = fieldName;
        this.fixedCount = null;
    }
    public BufferBT(Integer fixedCount) {
        super();
        this.fixedCount = fixedCount;
        this.fieldName = null;
    }

    @Override
    public List<PacketField> asPacketFields() {
      //  AbstractBehavioural behavioural = super.
     //   DeserializerRef readBuffer = new DeserializerRef();
        return List.of();
    }
}
