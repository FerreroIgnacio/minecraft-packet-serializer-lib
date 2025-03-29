package Behaviourals;

import Generic.GenericPath;
import Serialization.PacketField;
import SerializationInfo.Refs.Components.UnsafeComponent;
import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;
import SerializationInfo.SerializationInfo;

import java.util.LinkedHashMap;
import java.util.List;

public class BitfieldComponentBT extends AbstractBehavioural{

    private final int size;
    private final boolean signed;
    public BitfieldComponentBT(int size, boolean signed) {
        super(new LinkedHashMap<>());
        this.size = size;
        this.signed = signed;
    }

    public int getSize() {
        return size;
    }

    public boolean isSigned() {
        return signed;
    }

    @Override
    public List<PacketField> asPacketFields() {
        SerializationInfo aux = new SerializationInfo(Integer.class, new SerializerRef(new UnsafeComponent("")), new DeserializerRef(new UnsafeComponent("")));
        return List.of(new PacketField(this.getName(), aux));
    }

    @Override
    protected String getName() {
        return getPath().getIndexSegment(getPath().getLength() - 2) + "_" + getPath().getLastSegment();
    }
}
