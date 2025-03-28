package Behaviourals;

import Generic.BehaviouralNavigationException;
import Generic.GenericPath;
import Serialization.PacketField;

import java.util.*;

public abstract class AbstractBehavioural {
    private AbstractBehavioural father;
    private final LinkedHashMap<String, ? extends AbstractBehavioural> children;
    private GenericPath path;

    public AbstractBehavioural(LinkedHashMap<String, ? extends AbstractBehavioural> children) {
        this.children = children;
        this.path = new GenericPath();
        for(Map.Entry<String, ? extends AbstractBehavioural> childEntry : children.entrySet()) {
            childEntry.getValue().setFather(childEntry.getKey(), this);
        }
    }

    public AbstractBehavioural() {
        this(new LinkedHashMap<>());
    }

    protected void setFather(String keyOfSelf, AbstractBehavioural father) {
        this.father = father;
        this.updatePath(keyOfSelf);
    }
    protected void updatePath(String keyOfSelf) {
        AbstractBehavioural father = getFather();
        this.path = father != null ? father.getPath().append(keyOfSelf) : new GenericPath();
        for(Map.Entry<String, ? extends AbstractBehavioural> childEntry: children.entrySet()){
            childEntry.getValue().updatePath(childEntry.getKey());
        }
    }
    public GenericPath getPath() {
        return path;
    }

    protected AbstractBehavioural getFather() {
        return father;
    }

    public Map<String, ? extends AbstractBehavioural> getChildren() {
        return children;
    }

    public abstract List<PacketField> asPacketFields();
/*
    protected AbstractBehavioural resolvePath(GenericPath path) {
        if(path.toString().equals("/")) {
            return this;
        }
        if(path.getFirstSegment().equals("..")){
            if(father == null)
                throw new BehaviouralNavigationException("Attempting to acess father of fatherless behavioural");
            return father.resolvePath(path);
        }
        if(father.getChildren().containsKey(path.getFirstSegment())){
            return father.getChildren().get(path.getFirstSegment()).resolvePath(path.consumeFirst());
        } else {
            throw new BehaviouralNavigationException("Attempting to access child " +path.getFirstSegment() + " but only " + children.keySet() + " are available");
        }
    }
    protected AbstractBehavioural resolvePath(String pathString) {
        return resolvePath(new GenericPath(pathString));
    }
    */

}
