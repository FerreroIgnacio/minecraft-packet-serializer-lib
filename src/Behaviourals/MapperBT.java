package Behaviourals;

import Serialization.PacketField;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapperBT extends AbstractBehavioural {
    public MapperBT(Map<String, String> mappings, ClassBT type) {
        super();
        this.type = type;
    }
    ClassBT type;
    @Override
    public List<PacketField> asPacketFields() {
        return type.asPacketFields().stream().map(p -> p.addGenerator(this)).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapperBT)) return false;
        if (!super.equals(o)) return false;
        MapperBT that = (MapperBT) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}
