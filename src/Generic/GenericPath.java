package Generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericPath {
    private final List<String> segments;
    public GenericPath(List<String> segments) {
        this.segments = List.copyOf(segments);
    }
    public GenericPath(String path) {
        this.segments = Arrays.asList(path.split("/"));
    }
    public GenericPath() {
        this.segments = new ArrayList<>();
       // segments.add("");
    }
    public GenericPath(List<String> segments, String... extraSegments) {
        List<String> segmentsCopy = new ArrayList<>(segments);
        segmentsCopy.addAll(Arrays.asList(extraSegments));
        this.segments = segmentsCopy;
    }

    @Override
    public String toString() {
        return "/" + String.join("/", segments);
    }
    public String getLastSegment() {
        return segments.getLast();
    }
    public String getFirstSegment() {
        return segments.getFirst();
    }

    public GenericPath append(String segment) {
        return new GenericPath(segments, segment);
    }
    public GenericPath consumeFirst(){
        return new GenericPath(segments.subList(1, segments.size()));
    }

    public int getLength() {
        return segments.size();
    }
    public String getIndexSegment(int index) {
        return segments.get(index);
    }
}
