import java.util.Set;

public class PacketBase {
    private final int size;
    private final int id;
    private final String name;
    private final Set<String> version;
    public PacketBase(int size, String name, Set<String> version, int id) {
        this.size = size;
        this.name = name;
        this.version = version;
        this.id = id;
    }

    public Set<String> getVersion() {
        return version;
    }
}
