package Generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericPath {
   // private final List<String> segments;
    private final String path;
  /*  public GenericPath(List<String> segments) {
        this.segments = List.copyOf(segments);
    }*/
    public GenericPath(String path) {
        this.path = path;
    }
    public GenericPath() {
        this.path = "";
       // segments.add("");
    }

    @Override
    public String toString() {
        return path;
    }
    public String getLastSegment() {
        String aux = path.substring(path.lastIndexOf('/') + 1);
        return aux == null ? "" : aux;
    }
    public String getFirstSegment() {
        String pathStr = path;
        if(path.startsWith("/"))
            pathStr = pathStr.substring(1);
        if(pathStr.indexOf('/') == -1)
            return pathStr;
        return pathStr.substring(0, pathStr.indexOf('/'));
    }

    public GenericPath append(String segment) {
        return new GenericPath(path + (path.isEmpty() ? "" : "/") + segment);
    }
    public GenericPath consumeFirst() {
        if (path.isEmpty()) return this;

        String pathStr = path.startsWith("/") ? path.substring(1) : path;
        int index = pathStr.indexOf('/');

        return index == -1 ? new GenericPath("") : new GenericPath(pathStr.substring(index + 1));
    }

    public int getLength() {
        return path.split("/").length;
    }
    public String getIndexSegment(int index) {
        return path.split("/")[index];
    }
}
