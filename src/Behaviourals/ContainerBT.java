package Behaviourals;

import Generic.BehaviouralNavigationException;
import Generic.GenericPath;
import Serialization.PacketField;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContainerBT extends AbstractBehavioural {
    private boolean isDirectoryLevel;

    public boolean isDirectoryLevel() {
        return isDirectoryLevel;
    }
    public void disableDirectoryLevel() {
      //  isDirectoryLevel = false;
    }

    public ContainerBT(LinkedHashMap<String, AbstractBehavioural> children) {
        super(children);
        isDirectoryLevel = true;
    }


    @Override
    public List<PacketField> asPacketFields() {
        return getChildren().values().stream()
                .map(AbstractBehavioural::asPacketFields)
                .flatMap(List::stream).map(p -> p.addGenerator(this)).collect(Collectors.toList());
    }
}
