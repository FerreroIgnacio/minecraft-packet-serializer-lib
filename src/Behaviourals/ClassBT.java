package Behaviourals;

import Serialization.PacketField;
import SerializationInfo.SerializationInfo;

import java.util.List;

public class ClassBT extends AbstractBehavioural {

    SerializationInfo serializationInfo;

    public ClassBT(SerializationInfo serializationInfo) {
        super();
        this.serializationInfo = serializationInfo;
    }
    public List<PacketField> asPacketFields(){
        return List.of(new PacketField(
                super.getPath().getLastSegment(),
                serializationInfo)
        );
    }

    @Override
    public String toString() {
        return "ClassBT{" +
                "serializationInfo=" + serializationInfo +
                '}';
    }
}
