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
        for(BehaviouralType child : children.values()) {
            child.setFather(this);
        }
    }

    public BehaviouralType() {
        this(Collections.emptyMap());
    }

    private void setFather(BehaviouralType father) {
        this.father = father;
        this.updatePath();
    }
    private void updatePath(){
        BehaviouralType father = getFather();
        this.path = father != null ? father.getPath() : new GenericPath();
        for(BehaviouralType child : father.children.values()){
            child.updatePath();
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
