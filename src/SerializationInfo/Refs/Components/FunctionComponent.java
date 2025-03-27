package SerializationInfo.Refs.Components;

import java.awt.*;
import java.util.Arrays;

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
}
