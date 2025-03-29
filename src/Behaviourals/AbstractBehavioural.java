package Behaviourals;

import Generic.BehaviouralNavigationException;
import Generic.GenericPath;
import Serialization.PacketField;

import java.util.*;

public abstract class AbstractBehavioural {
    private AbstractBehavioural father;
    private final Map<String, AbstractBehavioural> children;
    private GenericPath path;
    private String name;

    public AbstractBehavioural(Map<String, AbstractBehavioural> children) {
        this.children = children;
        this.path = new GenericPath();
        for (Map.Entry<String, ? extends AbstractBehavioural> childEntry : children.entrySet()) {
            childEntry.getValue().setFather(childEntry.getKey(), this);
        }
        this.name = "";
    }

    public AbstractBehavioural() {
        this(Collections.emptyMap());
    }

    protected void setFather(String keyOfSelf, AbstractBehavioural father) {
        this.father = father;
        this.updatePath(keyOfSelf);
    }

    protected void updatePath(String keyOfSelf) {
        AbstractBehavioural father = getFather();
        this.path = father != null ? father.getPath().append(keyOfSelf) : new GenericPath();
        this.name = path.getLastSegment();
        for (Map.Entry<String, ? extends AbstractBehavioural> childEntry : children.entrySet()) {
            childEntry.getValue().updatePath(childEntry.getKey());
        }
    }

    public GenericPath getPath() {
        return path;
    }

    protected AbstractBehavioural getFather() {
        return father;
    }

    public Map<String, AbstractBehavioural> getChildren() {
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
    protected String getName(){
        return path.getLastSegment();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChildren());
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AbstractBehavioural that = (AbstractBehavioural) obj;
        return Objects.equals(children, that.children);
    }
}
