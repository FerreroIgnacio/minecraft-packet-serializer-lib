package Behaviourals;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractBehavioural {
    private AbstractBehavioural father;
    private final Map<String, AbstractBehavioural> children;
    private GenericPath path;

    public AbstractBehavioural(Map<String, AbstractBehavioural> children) {
        this.children = children;
        this.path = new GenericPath();
        for(Map.Entry<String, AbstractBehavioural> childEntry : children.entrySet()) {
            childEntry.getValue().setFather(childEntry.getKey(), this);
        }
    }

    public AbstractBehavioural() {
        this(Collections.emptyMap());
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
