package Behaviourals;

import Serialization.PacketField;

import java.util.List;
import java.util.Map;

public class ArrayBT extends AbstractBehavioural{
    AbstractBehavioural countType;
    String countFieldPath;

    public ArrayBT(AbstractBehavioural countType, AbstractBehavioural children) {
        super(Map.of("content", children), true);
        this.countType = countType;
    }
    public ArrayBT(String countFieldPath, AbstractBehavioural children) {
        super(Map.of("content", children), true);
        this.countFieldPath = countFieldPath;
    }

    @Override
    public List<PacketField> asPacketFields() {
        return List.of();
    }
}
