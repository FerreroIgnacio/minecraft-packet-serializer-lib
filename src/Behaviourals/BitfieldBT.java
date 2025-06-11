package Behaviourals;

import Generic.BehaviouralNavigationException;
import Generic.GenericPath;
import Serialization.Natives;
import Serialization.PacketField;
import SerializationInfo.Refs.Components.EqualsComponent;
import SerializationInfo.Refs.Components.UnsafeComponent;
import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;
import SerializationInfo.SerializationInfo;

import java.util.*;

public class BitfieldBT extends AbstractBehavioural {


    public BitfieldBT(LinkedHashMap<String, AbstractBehavioural> children) {
        super(children);
    }
    public List<PacketField> asPacketFields() {
        int totalSize = 0;
        for(Map.Entry<String, ? extends AbstractBehavioural> entry: getChildren().entrySet()) {
            if(entry.getValue() instanceof BitfieldComponentBT bfc) {
                totalSize += bfc.getSize();
            } else {
                throw new RuntimeException("Bitfield child " + entry.getKey() + " is not a BitfieldComponentBT");
            }
        }
        Natives totalValueEnum;
        Class<?> totalClazz;
        switch(totalSize) {
            case 8 -> {
                totalValueEnum = Natives.I8;
                totalClazz = byte.class;
            }
            case 16 -> {
                totalValueEnum = Natives.I16;
                totalClazz = short.class;
            }
            case 32 -> {
                totalValueEnum = Natives.I32;
                totalClazz = int.class;
            }
            case 64 -> {
                totalValueEnum = Natives.I64;
                totalClazz = long.class;
            }
            default -> throw new IllegalArgumentException("Invalid bitfield size: " + totalSize);
        }

        DeserializerRef totalDeserializerRef = totalValueEnum.getSerializationInfo().getDeserializerRef();
        SerializerRef totalSerializerRef = new SerializerRef(new EqualsComponent("1", 1));
        SerializationInfo totalInfo = new SerializationInfo(
                totalClazz,
                totalSerializerRef,
                totalDeserializerRef);

        String totalName = getPath().getLastSegment();

        PacketField totalField = new PacketField(
                totalName,
                totalInfo, List.of(this));
        List<PacketField> returnList = new ArrayList<>();
        returnList.add(totalField);

        for(String key: getChildren().keySet()) {
            int fieldSize = ((BitfieldComponentBT)getChildren().get(key)).getSize();
            boolean signed = ((BitfieldComponentBT)getChildren().get(key)).isSigned();
            DeserializerRef deserializerRef = new DeserializerRef(
                    new UnsafeComponent(
                         "((1L << " + fieldSize + " ) - 1)"
                            + " & "
                            + "(" + totalName + " >> (" + totalSize + " - " + fieldSize + "))"
                    ));
            SerializerRef serializerRef = new SerializerRef(new EqualsComponent("1", 1));
            SerializationInfo fieldSerInfo = new SerializationInfo(Integer.class, serializerRef, deserializerRef);
            String str = super.getPath().getLastSegment();
            PacketField p = new PacketField(
                    (str.isEmpty() ? "" : super.getPath().getLastSegment() + "_") + key,
                    fieldSerInfo
                    , List.of(this));
            returnList.add(p);
        }
        return returnList;
    }



}

