package Behaviourals;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContainerBT extends BehaviouralType{
    public ContainerBT(Map<String, BehaviouralType> children) {
        super(children);
    }

    @Override
    public List<PacketField> asPacketFields() {
        return getChildren().values().stream()
                .map(BehaviouralType::asPacketFields)
                .flatMap(List::stream).collect(Collectors.toList());
    }
}
