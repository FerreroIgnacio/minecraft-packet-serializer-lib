package Behaviourals;

import java.util.Map;

public class ArrayBT extends AbstractBehavioural{
    AbstractBehavioural countType;
    String countFieldPath;

    public ArrayBT(AbstractBehavioural countType, AbstractBehavioural children) {
        super(Map.of("content", children), true);
        this.countType = countType;
        this.countFieldPath = countFieldPath;
    }
}
