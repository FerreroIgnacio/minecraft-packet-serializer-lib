package Behaviourals;

import Serialization.PacketField;
import SerializationInfo.SerializationInfo;

import java.util.Collections;
import java.util.List;
import SerializationInfo.ClassDescriptor;

public class ClassBT extends AbstractBehavioural {

    private final SerializationInfo serializationInfo;

    public ClassBT(SerializationInfo serializationInfo) {
        super();
        this.serializationInfo = serializationInfo;
    }

    private final static ClassDescriptor voidClassDesc = new ClassDescriptor(Void.class);

    public List<PacketField> asPacketFields(){
        if(serializationInfo.getClassDescriptor().equals(voidClassDesc))
            return Collections.emptyList();
        return List.of(new PacketField(
                getName(),
                serializationInfo)
        );
    }

    public SerializationInfo getSerializationInfo() {
        return serializationInfo;
    }

    @Override
    public String toString() {
        return serializationInfo.toString();
    }
}
