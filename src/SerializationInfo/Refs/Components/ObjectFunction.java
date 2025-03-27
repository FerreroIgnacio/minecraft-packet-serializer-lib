package SerializationInfo.Refs.Components;

public class ObjectFunction implements RefComponent {
    private final String name;
    private final String function;
    private final String[] args;
    public ObjectFunction(String name, String function, String[] args) {
        this.name = name;
        this.function = function;
        this.args = args;
    }
    public String toString() {
        return name + "." + function + "(" + String.join(", ", args) + ")";
    }
}
