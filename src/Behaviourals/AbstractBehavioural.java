package Behaviourals;

import Generic.BehaviouralNavigationException;
import Generic.GenericPath;
import Serialization.PacketField;

import java.util.*;

import com.google.common.annotations.VisibleForTesting;
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
        if(father == null) {
            throw new BehaviouralNavigationException("Attempting to access father of root object");
        }
        return father;
    }

    public Map<String, AbstractBehavioural> getChildren() {
        return children;
    }

    public abstract List<PacketField> asPacketFields();


    public AbstractBehavioural resolvePath(GenericPath path) {
        if (path.toString().isEmpty()) {
            return this;
        }

        String firstSegment = path.getFirstSegment();

        // Only ContainerBT and BitfieldBT consume ".."
        if ((this instanceof ContainerBT || this instanceof BitfieldBT) && firstSegment.equals("..")) {
            return getFather().resolvePath(path.consumeFirst());
        }

        // If this is a ContainerBT or BitfieldBT, navigate its children
        if (this instanceof ContainerBT || this instanceof BitfieldBT) {
            AbstractBehavioural child = getChildren().get(firstSegment);
            if (child != null) {
                return child.resolvePath(path.consumeFirst());
            }
            throw new BehaviouralNavigationException("Attempting to resolve child '" + firstSegment + "' but only " + getChildren().keySet() + " available");
        }

        // Otherwise, delegate resolution to parent without consuming ".."
        return getFather().resolvePath(path);
    }

    public AbstractBehavioural resolvePath(String path) {
        return this.resolvePath(new GenericPath(path));
    }

    protected String getName() {
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + System.identityHashCode(this) + children.keySet().toString();
    }

    @VisibleForTesting
    private AbstractBehavioural get(String key) {
        if(children.containsKey(key)) {
            return children.get(key);
        }
        throw new RuntimeException("DEBUG missing children");
    }
}
