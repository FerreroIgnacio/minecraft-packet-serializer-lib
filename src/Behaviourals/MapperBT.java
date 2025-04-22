package Behaviourals;

import Serialization.PacketField;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapperBT extends AbstractBehavioural {
    public MapperBT(Map<String, String> mappings, ClassBT type) {
        super();
        this.type = type;
    }
    ClassBT type;
    @Override
    public List<PacketField> asPacketFields() {
        return type.asPacketFields();
    }
}
