package Serialization;

import SerializationInfo.SerializationInfo;

import java.util.Objects;

public class PacketField {
    private final String name;
    private final SerializationInfo serializationInfo;

    public PacketField(String name, SerializationInfo serializationInfo) {
        this.name = name;
        this.serializationInfo = serializationInfo;
    }

    @Override
    public String toString() {
     /*   return "PacketField{" +
                "name='" + name + '\'' +
                ", serializationInfo=" + serializationInfo +
                '}';*/
        return serializationInfo.getClassDescriptor() + " " + name + " = " + serializationInfo.getDeserializerRef();
    }

    public String simpleRef(){
        return serializationInfo.getClassDescriptor() + " " + name;
    }

    public SerializationInfo getSerializationInfo() {
        return serializationInfo;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PacketField that = (PacketField) o;
        return Objects.equals(name, that.name) && Objects.equals(serializationInfo, that.serializationInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, serializationInfo);
    }
}
