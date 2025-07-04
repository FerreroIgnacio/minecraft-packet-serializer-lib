package Behaviourals;

import Generic.BehaviouralNavigationException;
import Generic.GenericPath;
import Serialization.PacketField;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
public abstract class AbstractBehavioural {
    private AbstractBehavioural father;
    private final LinkedHashMap<String, AbstractBehavioural> children;



    private GenericPath path;
    private String name;
    private final boolean hiddenChildren;
    public AbstractBehavioural(LinkedHashMap<String, AbstractBehavioural> children) {
        this(children, false);
    }
    public AbstractBehavioural(LinkedHashMap<String, AbstractBehavioural> children, boolean hiddenChildren) {
        this.children = children;
        this.hiddenChildren = hiddenChildren;
        this.path = new GenericPath();
        this.name = getName();
        for(Map.Entry<String, ? extends AbstractBehavioural> childEntry : children.entrySet()) {
            childEntry.getValue().setFather(childEntry.getKey(), this);
        }
    }
    public AbstractBehavioural() {
        this(new LinkedHashMap<>(Collections.emptyMap()));
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
        /*if(father == null) {
            throw new BehaviouralNavigationException("Attempting to access father of root object");
        }*/
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

        if (((this instanceof ContainerBT cbt && cbt.isDirectoryLevel())|| this instanceof BitfieldBT || this instanceof Bitflags) && firstSegment.equals("..")) {
            return getFather().resolvePath(path.consumeFirst());
        }

        // If this is a ContainerBT or BitfieldBT, navigate its children
        if ((this instanceof ContainerBT cbt && cbt.isDirectoryLevel()) || this instanceof BitfieldBT || this instanceof Bitflags) {
            AbstractBehavioural child = getChildren().get(firstSegment);
            if(hiddenChildren){
           //     throw new BehaviouralNavigationException("Attempting to access child of hidden children of " + this);
            }
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

        String lSeg = path.getLastSegment();
        if(lSeg.equals("anon")) {
            AbstractBehavioural father = getFather();
            if (father != null) {
                lSeg = father.getName();
            }
        }
        return lSeg.equals("default") ? "_default" : lSeg.replace(":", "$");
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
        Iterator<Map.Entry<String, AbstractBehavioural>> thatIterator = that.getChildren().entrySet().iterator();
        Iterator<Map.Entry<String, AbstractBehavioural>> thisIterator = getChildren().entrySet().iterator();

        while(thatIterator.hasNext() && thisIterator.hasNext()) {
            Map.Entry<String, AbstractBehavioural> thatEntry = thatIterator.next();
            Map.Entry<String, AbstractBehavioural> thisEntry = thisIterator.next();

            if(!thatEntry.equals(thisEntry)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + System.identityHashCode(this) + children.keySet().toString();
    }




    @VisibleForTesting
    public AbstractBehavioural get(String key) {
        if(children.containsKey(key)) {
            return children.get(key);
        }
        throw new RuntimeException("DEBUG missing children");
    }

    public List<String> getFieldNames(){
        return new ArrayList<>(children.keySet());
    }


    @VisibleForTesting
    public boolean isBuildable() {
        for(AbstractBehavioural child : children.values()) {
            if(!child.isBuildable()) {
                return false;
            }
        }
        return true;
    }
    @VisibleForTesting
    public AbstractBehavioural getUnbuiltReason() {
        for (AbstractBehavioural child : children.values()) {
            // if child itself is unbuildable…
            if (!child.isBuildable()) {
                // see if there’s a deeper cause…
                AbstractBehavioural deeper = child.getUnbuiltReason();
                return (deeper != null) ? deeper : child;
            }
        }
        // no unbuildable children means this subtree is fine
        return this;
    }

    // Stub: Returns null by default, override in subclasses if needed
    public SerializationInfo.ClassDescriptor getClassDescriptor() {
        return null;
    }
    // Stub: Returns empty string by default, override in subclasses if needed
    public String generateFieldDeclarations() {
        return "";
    }
    public String generateConstructors() {
        return "";
    }
    public String generateGetters() {
        return "";
    }
}
