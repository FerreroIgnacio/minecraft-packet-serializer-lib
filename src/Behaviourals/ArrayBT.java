package Behaviourals;

import Serialization.PacketField;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ArrayBT extends AbstractBehavioural{
    ClassBT countType;
    String countFieldPath;
    AbstractBehavioural type;
    Integer fixedCount;
    public ArrayBT(AbstractBehavioural countType, AbstractBehavioural child) {
        super(Map.of("content", child), true);
        if((!(countType instanceof ClassBT))){
            ClassBT classbt = (ClassBT)countType;
            Class<?> clazz = classbt.getSerializationInfo().getClassDescriptor().getClazz();
            if(clazz != Integer.class && clazz != Long.class && clazz != Byte.class && clazz != Short.class) {
                throw new RuntimeException("Attempting to create an array with non integer countType");
            }
        }
        this.countType = (ClassBT) countType;
        countFieldPath = null;
        this.type = child;
        fixedCount = null;
    }
    public ArrayBT(String countFieldPath, AbstractBehavioural child) {
        super(Map.of("content", child), true);
       // this.countType = (ClassBT)resolvePath(countFieldPath);
        this.countFieldPath = countFieldPath;
        countType = null;
        this.type = child;
        fixedCount = null;
    }
    public ArrayBT(int fixedCount, AbstractBehavioural child) {
        super(Map.of("content", child), true);
        this.countFieldPath = null;
        countType = null;
        this.fixedCount = fixedCount;
    }

    @Override
    public List<PacketField> asPacketFields() {
        AbstractBehavioural countType = this.countType == null ? resolvePath(countFieldPath) : this.countType;
        List<PacketField> pfs = type.asPacketFields();
        for(PacketField pf : pfs){
            pf.getSerializationInfo().getClassDescriptor().arraify();
        }
        return pfs;
    }
}
