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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TernaryRefComponent that = (TernaryRefComponent) o;
        return Objects.equals(conditions, that.conditions) && Objects.equals(conditionNames, that.conditionNames) && Objects.equals(left, that.left) && Objects.equals(right, that.right) && Objects.equals(joinOperator, that.joinOperator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditions, conditionNames, left, right, joinOperator);
    }
}
