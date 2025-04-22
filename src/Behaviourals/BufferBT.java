package Behaviourals;

import Generic.Consts;
import Serialization.PacketField;
import SerializationInfo.Refs.Components.FunctionComponent;
import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;
import SerializationInfo.SerializationInfo;

import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

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
        //throw new UnsupportedOperationException("Not supported yet.");
        SerializationInfo sInfo = new SerializationInfo(
                BitSet.class,
                new SerializerRef(new FunctionComponent("SerializerFunctions.writeBuffer", Consts.BUFNAME.toString())),
                new DeserializerRef(new FunctionComponent("DeserializerFunctions.readBuffer", Consts.BUFNAME.toString()))
        );
        return List.of(new PacketField(getName(), sInfo));
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BufferBT)) return false;
        if (!super.equals(o)) return false;      // include if AbstractBehavioural has its own equals()
        BufferBT that = (BufferBT) o;
        return size == that.size &&
                Objects.equals(countType, that.countType) &&
                Objects.equals(countPath, that.countPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), size, countType, countPath);
    }
}
