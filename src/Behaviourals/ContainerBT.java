package Behaviourals;

import Generic.BehaviouralNavigationException;
import Generic.GenericPath;
import Serialization.PacketField;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContainerBT extends AbstractBehavioural {
    public ContainerBT(LinkedHashMap<String, AbstractBehavioural> children) {
        super(children);
    }

    @Override
    public List<PacketField> asPacketFields() {
        return getChildren().values().stream()
                .map(AbstractBehavioural::asPacketFields)
                .flatMap(List::stream).collect(Collectors.toList());
    }


/*
    @Override
    public AbstractBehavioural resolvePath(GenericPath path) {
        if (path.toString().isEmpty())
            return this;
        if (path.toString().startsWith("..")) {
            if (getFather() == null) {
                throw new BehaviouralNavigationException("Attempting to access father of fatherless object");
            }
            return getFather().resolvePath(path.consumeFirst());
        } else {
            return getChildren().get(path.getFirstSegment()).resolvePath(path.consumeFirst());
        }
    }
    */

}
