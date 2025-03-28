package Behaviourals;

import Generic.BehaviouralNavigationException;
import Generic.GenericPath;
import Serialization.PacketField;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContainerBT extends AbstractBehavioural {
    public ContainerBT(Map<String, AbstractBehavioural> children) {
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
    protected AbstractBehavioural resolvePath(GenericPath path) {
        if(path.toString().equals("/")) {
            return this;
        }
        if(path.getFirstSegment().equals("..")){
            if(getFather() == null)
                throw new BehaviouralNavigationException("Attempting to acess father of fatherless behavioural");
            return getFather().resolvePath(path.consumeFirst());
        }
        if(getChildren().containsKey(path.getFirstSegment())){
            return getChildren().get(path.getFirstSegment()).resolvePath(path.consumeFirst());
        } else {
            throw new BehaviouralNavigationException("Attempting to access child " +path.getFirstSegment() + " but only " + getChildren().keySet() + " are available");
        }
    }
    */

}
