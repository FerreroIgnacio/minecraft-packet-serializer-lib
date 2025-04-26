import Behaviourals.AbstractBehavioural;
import Serialization.PacketField;

import java.util.List;
import java.util.Objects;

public class Packet {
    private final AbstractBehavioural content;
    private final String name;
    private final String version;

    public Packet(AbstractBehavioural content, String name, String version) {
        this.content = content;
        this.name = name;
        this.version = version;
    }

    @Override
    public String toString() {
        return name + "_" +  version + " : " + content.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Packet packet = (Packet) o;
        return Objects.equals(content, packet.content);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(content);
    }
    public List<PacketField> asPacketFields(){
        return content.asPacketFields();
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }
}
