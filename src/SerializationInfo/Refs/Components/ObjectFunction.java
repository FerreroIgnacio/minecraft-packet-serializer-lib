package SerializationInfo.Refs.Components;

public class ObjectFunction extends FunctionComponent {
    private final String name;
    public ObjectFunction(String name, String function, String[] args) {
        super(function, args);
        this.name = name;
    }
    public String toString() {
        return name + "." + super.getFunction() + "(" + String.join(", ", super.getArgs()) + ")";
    }
}
