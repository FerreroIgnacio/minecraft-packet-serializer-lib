package Behaviourals;

import Serialization.PacketField;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OptionBT extends AbstractBehavioural {
    public OptionBT(AbstractBehavioural child) {
        super(new LinkedHashMap<>(Map.of("content", child)), true);
    }

    @Override
    public List<PacketField> asPacketFields() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
