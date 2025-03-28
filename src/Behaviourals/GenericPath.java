package Behaviourals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericPath {
    private final List<String> segments;
    public GenericPath(List<String> segments) {
        this.segments = List.copyOf(segments);
    }
    public GenericPath(String... segments) {
        this.segments = Arrays.asList(segments);
    }
    public GenericPath() {
        this.segments = new ArrayList<>();
        segments.add("");
    }
    public GenericPath(List<String> segments, String... extraSegments) {
        List<String> segmentsCopy = new ArrayList<>(segments);
        segmentsCopy.addAll(Arrays.asList(extraSegments));
        this.segments = segmentsCopy;
    }

    @Override
    public String toString() {
        return String.join("/", segments);
    }
    public String getLastSegment() {
        return segments.getLast();
    }

    public GenericPath append(String segment) {
        return new GenericPath(segments, segment);
    }
}
