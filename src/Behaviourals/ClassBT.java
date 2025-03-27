package Behaviourals;

import SerializationInfo.SerializationInfo;

import java.util.List;
import java.util.Map;

public class ClassBT extends BehaviouralType{

    SerializationInfo serializationInfo;

    public ClassBT(SerializationInfo serializationInfo) {
        super();
    }
    public List<PacketField> asPacketFields(){
        return List.of(new PacketField(
                super.getPath().getLastSegment(),
                serializationInfo)
        );
    }

}
