package Behaviourals;

import Generic.GenericPath;
import Serialization.PacketField;
import SerializationInfo.Refs.Components.UnsafeComponent;
import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;
import SerializationInfo.SerializationInfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

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
        return List.of(new PacketField(this.getName(), aux, List.of(this)));
    }

    @Override
    protected String getName() {
        if(getPath().getLength() < 2){
            return getPath().getLastSegment();
        }
        String prefix =  getPath().getIndexSegment(getPath().getLength() - 2) + "_";

        return (prefix.equals("_") ? "" : prefix) + getPath().getLastSegment();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BitfieldComponentBT that = (BitfieldComponentBT) obj;
        return size == that.getSize() && signed == that.isSigned();
    }

}
