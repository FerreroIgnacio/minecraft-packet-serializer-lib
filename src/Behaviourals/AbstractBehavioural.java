package Behaviourals;

import Generic.GenericPath;
import Serialization.PacketField;

import java.util.*;

public abstract class AbstractBehavioural {
    private AbstractBehavioural father;
    private final LinkedHashMap<String, AbstractBehavioural> children;
    private GenericPath path;

    public AbstractBehavioural(LinkedHashMap<String, AbstractBehavioural> children) {
        this.children = children;
        this.path = new GenericPath();
        for(Map.Entry<String, AbstractBehavioural> childEntry : children.entrySet()) {
            childEntry.getValue().setFather(childEntry.getKey(), this);
        }
    }

    public AbstractBehavioural() {
        this(new LinkedHashMap<>());
    }

    private void setFather(String keyOfSelf, AbstractBehavioural father) {
        this.father = father;
        this.updatePath(keyOfSelf);
    }
    private void updatePath(String keyOfSelf) {
        AbstractBehavioural father = getFather();
        this.path = father != null ? father.getPath().append(keyOfSelf) : new GenericPath();
        for(Map.Entry<String, AbstractBehavioural> childEntry: children.entrySet()){
            childEntry.getValue().updatePath(childEntry.getKey());
        }
    }
    public GenericPath getPath() {
        return path;
    }

    private AbstractBehavioural getFather() {
        return father;
    }

    public Map<String, AbstractBehavioural> getChildren() {
        return children;
    }

    public abstract List<PacketField> asPacketFields();
}
