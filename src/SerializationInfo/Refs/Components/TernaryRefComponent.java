package SerializationInfo.Refs.Components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TernaryRefComponent {
    List<Condition> conditions;
    List<String> conditionNames;
    RefComponent left;
    RefComponent right;

    public TernaryRefComponent(List<Condition> conditions, RefComponent left, RefComponent right) {
        this.conditions = conditions;
        this.left = left;
        this.right = right;
        this.conditionNames = conditions.stream().map(s -> s.toString()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "(" + String.join(" && ",conditionNames) +  " ? " + left + " : " + right + ")";
    }
}
