package Behaviourals;

import Serialization.PacketField;

import java.util.LinkedHashMap;
import java.util.List;

public class BufferBT extends AbstractBehavioural{
    public BufferBT(int size) {
        super();
        this.size = size;
    }
    int size;
    ClassBT countType;
    String countPath;
    public BufferBT(ClassBT countType) {
        this.countType = countType;
    }
    public BufferBT(String countPath) {
        this.countPath = countPath;
    }

    @Override
    public List<PacketField> asPacketFields() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
