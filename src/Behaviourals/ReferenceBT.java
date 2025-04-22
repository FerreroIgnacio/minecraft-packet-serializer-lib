package Behaviourals;

import Generic.Consts;
import Serialization.PacketField;
import SerializationInfo.Refs.Components.FunctionComponent;
import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;
import SerializationInfo.SerializationInfo;

import java.util.List;
import java.util.Map;

public class ReferenceBT extends  AbstractBehavioural{
    private final String name;
    private final AbstractBehavioural type;

    public ReferenceBT(String name, AbstractBehavioural type) {
        super();
        this.name = name;
        this.type = type;
    }

    @Override
    public List<PacketField> asPacketFields() {
        return List.of(
                new PacketField(
                        getName(),
                        new SerializationInfo(
                                name,
                                new SerializerRef(new FunctionComponent(name + Consts.CUSTOMCLASSWRITEMETHOD.getName())),
                                new DeserializerRef(new FunctionComponent(name + Consts.CUSTOMCLASSREADMETHOD.getName()))
                        ))
        );
    }

    @Override
    public String toString() {
        return name;
    }
}
