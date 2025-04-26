package SerializationInfo.Refs.Components;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

public class FunctionComponent implements RefComponent {
    private final String function;
    private final String[] args;

    public FunctionComponent(String function, String... args) {
        this.function = function;
        this.args = args;
    }

    public String toString() {
        return function + "(" + String.join(", ", args) + ")";
    }

    protected String getFunction() {
        return function;
    }

    protected String[] getArgs() {
        return args;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FunctionComponent that = (FunctionComponent) o;
        return Objects.equals(function, that.function) && Objects.deepEquals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(function, Arrays.hashCode(args));
    }
}
