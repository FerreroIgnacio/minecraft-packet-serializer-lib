package Serialization;

import SerializationInfo.SerializationInfo;

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

    public SerializationInfo getSerializationInfo() {
        return serializationInfo;
    }

    public String getName() {
        return name;
    }
}
