package Behaviourals;

import Serialization.PacketField;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OptionBT extends AbstractBehavioural {
    public OptionBT(AbstractBehavioural child) {
        super(new LinkedHashMap<>(Map.of("content", child)), true);
        this.child = child;
    }
    private final AbstractBehavioural child;
    @Override
    public List<PacketField> asPacketFields() {
        return child.asPacketFields().stream().map(p -> p.addGenerator(this)).toList();
    }
}
