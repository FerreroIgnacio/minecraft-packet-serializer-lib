package Serialization;

import Behaviourals.AbstractBehavioural;
import SerializationInfo.SerializationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PacketField {
    private String name;
    private final SerializationInfo serializationInfo;

    private final List<AbstractBehavioural> generator;

    public PacketField(String name, SerializationInfo serializationInfo, List<AbstractBehavioural> generators) {
        this.name = name;
        this.serializationInfo = serializationInfo;
        this.generator = new ArrayList<>(generators);
    }
    public PacketField addGenerator(AbstractBehavioural g){
        generator.add(g);
        return this;
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

    public void setName(String name) {
        this.name = name;
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
