import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericPath {
    private final List<String> segments;
    public GenericPath(List<String> segments) {
        this.segments = segments;
    }
    public GenericPath(String... segments) {
        this.segments = Arrays.asList(segments);
    }
    public GenericPath() {
        this.segments = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.join("/", segments);
    }
}
