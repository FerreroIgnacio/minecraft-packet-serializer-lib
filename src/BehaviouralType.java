import java.util.Map;

public class BehaviouralType {
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
}
