package Behaviourals;

import Serialization.PacketField;
import SerializationInfo.Refs.Components.FunctionComponent;
import SerializationInfo.Refs.Components.UnsafeComponent;
import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;
import SerializationInfo.SerializationInfo;

import java.util.*;

public class ArrayBT extends AbstractBehavioural{
    private final AbstractBehavioural countType;
    private final String countFieldPath;
    private final AbstractBehavioural type;
    private final Integer fixedCount;
    public ArrayBT(AbstractBehavioural countType, AbstractBehavioural child) {
        super(new LinkedHashMap<>(Map.of("anon", child)), true);
        if((!(countType instanceof ClassBT)) && !(countType instanceof ReferenceBT)){
            ClassBT classbt = (ClassBT)countType;
            Class<?> clazz = classbt.getSerializationInfo().getClassDescriptor().getClazz();
            if(clazz != Integer.class && clazz != Long.class && clazz != Byte.class && clazz != Short.class) {
                throw new RuntimeException("Attempting to create an array with non integer countType");
            }
        }
        this.countType = countType;
        countFieldPath = null;
        this.type = child;
        fixedCount = null;
    }
    public ArrayBT(String countFieldPath, AbstractBehavioural child) {
        super(new LinkedHashMap<>(Map.of("anon", child)), true);
       // this.countType = (ClassBT)resolvePath(countFieldPath);
        this.countFieldPath = countFieldPath;
        countType = null;
        this.type = child;
        fixedCount = null;
    }
    public ArrayBT(int fixedCount, AbstractBehavioural child) {
        super(new LinkedHashMap<>(Map.of("anon", child)), true);
        this.countFieldPath = null;
        countType = null;
        this.fixedCount = fixedCount;
        this.type = child;
    }

    @Override
    public List<PacketField> asPacketFields() {

        List<PacketField> pfs = type.asPacketFields();
        List<PacketField> newPfs = new ArrayList<>();
        for(PacketField pf : pfs){
            //convert readInt() to readArr(aux -> readInt())

        //    SerializationInfo
            SerializerRef newSer = new SerializerRef(new UnsafeComponent("ArraySerialize()"));
            DeserializerRef newDeser = new DeserializerRef(new FunctionComponent("readArray", pf.getSerializationInfo().getDeserializerRef().toString()));
            newPfs.add(new PacketField(pf.getName(), new SerializationInfo(pf.getSerializationInfo().getClassDescriptor().copy(), newSer, newDeser), List.of(this)));
            newPfs.getLast().getSerializationInfo().getClassDescriptor().arraify();

        }
        return newPfs;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ArrayBT arrayBT = (ArrayBT) o;
        return Objects.equals(countType, arrayBT.countType) && Objects.equals(countFieldPath, arrayBT.countFieldPath) && Objects.equals(type, arrayBT.type) && Objects.equals(fixedCount, arrayBT.fixedCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), countType, countFieldPath, type, fixedCount);
    }
}
