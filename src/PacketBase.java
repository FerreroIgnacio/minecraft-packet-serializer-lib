public class PacketBase {
    private final int size;
    private final int id;
    private final String name;
    private final String version;
    public PacketBase(int size, String name, String version, int id) {
        this.size = size;
        this.name = name;
        this.version = version;
        this.id = id;
    }

    public String getVersion() {
        return version;
    }
}
