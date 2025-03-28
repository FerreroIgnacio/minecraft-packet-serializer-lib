package Behaviourals;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BehaviouralType {
    private BehaviouralType father;
    private final Map<String, BehaviouralType> children;
    private GenericPath path;

    public BehaviouralType(Map<String, BehaviouralType> children) {
        this.children = children;
        this.path = new GenericPath();
        for(Map.Entry<String, BehaviouralType> childEntry : children.entrySet()) {
            childEntry.getValue().setFather(childEntry.getKey(), this);
        }
    }

    public BehaviouralType() {
        this(Collections.emptyMap());
    }

    private void setFather(String keyOfSelf, BehaviouralType father) {
        this.father = father;
        this.updatePath(keyOfSelf);
    }
    private void updatePath(String keyOfSelf) {
        BehaviouralType father = getFather();
        this.path = father != null ? father.getPath().append(keyOfSelf) : new GenericPath();
        for(Map.Entry<String, BehaviouralType> childEntry: children.entrySet()){
            childEntry.getValue().updatePath(childEntry.getKey());
        }
    }
    public GenericPath getPath() {
        return path;
    }

    private BehaviouralType getFather() {
        return father;
    }

    public Map<String, BehaviouralType> getChildren() {
        return children;
    }

    public abstract List<PacketField> asPacketFields();
}
