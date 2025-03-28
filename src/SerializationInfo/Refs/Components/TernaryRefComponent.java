package SerializationInfo.Refs.Components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TernaryRefComponent implements RefComponent{
    private final List<Condition> conditions;
    private final List<String> conditionNames;
    private final RefComponent left;
    private final RefComponent right;
    private final String joinOperator;
    public TernaryRefComponent(List<Condition> conditions, RefComponent left, RefComponent right, String joinOperator) {
        this.conditions = conditions;
        this.left = left;
        this.right = right;
        this.joinOperator = joinOperator;
        this.conditionNames = conditions.stream().map(s -> s.toString()).collect(Collectors.toList());
    }

    public TernaryRefComponent(Condition condition, RefComponent left, RefComponent right) {
        this.conditions = List.of(condition);
        this.conditionNames = conditions.stream().map(s -> s.toString()).collect(Collectors.toList());
        this.left = left;
        this.right = right;
        this.joinOperator = "&&";
    }

    @Override
    public String toString() {
        return "(" + String.join(" "+joinOperator + " ",conditionNames) +  " ? " + left + " : " + right + ")";
    }
}
