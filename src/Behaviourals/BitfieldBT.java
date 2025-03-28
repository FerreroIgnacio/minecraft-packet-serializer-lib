package Behaviourals;

import Serialization.Natives;
import Serialization.PacketField;
import SerializationInfo.Refs.Components.EqualsComponent;
import SerializationInfo.Refs.Components.UnsafeComponent;
import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;
import SerializationInfo.SerializationInfo;

import java.util.*;

public class BitfieldBT extends AbstractBehavioural {

    Map<String, Integer> sizes;
    Map<String, Boolean> signed;
    public BitfieldBT(Map<String, Integer> size,
                      Map<String, Boolean> signed) {
        super(new LinkedHashMap<>());
        this.signed = signed;
        this.sizes = size;
    }
    public List<PacketField> asPacketFields() {
        int totalSize = 0;
        for(Map.Entry<String, Integer> entry: sizes.entrySet()) {
            totalSize += entry.getValue();
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
                totalInfo);
        List<PacketField> returnList = new ArrayList<PacketField>();
        returnList.add(totalField);

        for(String key: sizes.keySet()) {
            int fieldSize = sizes.get(key);
            boolean signed = this.signed.get(key);
            DeserializerRef deserializerRef = new DeserializerRef(
                    new UnsafeComponent(
                         "((1L << " + fieldSize + " ) - 1)"
                            + " & "
                            + "(" + totalName + " >> (" + totalSize + " - " + fieldSize + "))"
                    ));
            SerializerRef serializerRef = new SerializerRef(new EqualsComponent("1", 1));
            SerializationInfo fieldSerInfo = new SerializationInfo(Integer.class, serializerRef, deserializerRef);
            PacketField p = new PacketField(
                    super.getPath().getLastSegment() +
                            "_" + key,
                    fieldSerInfo
                    );
            returnList.add(p);
        }
        return returnList;
    }
}
