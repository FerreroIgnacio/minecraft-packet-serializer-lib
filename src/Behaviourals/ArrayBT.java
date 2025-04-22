package Behaviourals;

import Serialization.PacketField;

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
        AbstractBehavioural aux;

          //  AbstractBehavioural countType = this.countType == null ? resolvePath(countFieldPath) : this.countType;

            aux = this.type;

        List<PacketField> pfs = aux.asPacketFields();
        List<PacketField> newPfs = new ArrayList<>();
        for(PacketField pf : pfs){
            newPfs.add(new PacketField(pf.getName(), pf.getSerializationInfo().copy()));
            newPfs.getLast().getSerializationInfo().getClassDescriptor().arraify();

        }
        return newPfs;
    }
}
