package Behaviourals;

import Serialization.PacketField;
import SerializationInfo.Refs.Components.*;
import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;
import SerializationInfo.SerializationInfo;

import java.util.*;

public class SwitchBT extends AbstractBehavioural{

    private final String compareToPath;
    private final AbstractBehavioural defaultBehavioural;
    public SwitchBT(Map<String, AbstractBehavioural> cases, String compareToPath, AbstractBehavioural defaultBehavioural) {
        super(cases);
        getChildren().put("default", defaultBehavioural);
        defaultBehavioural.setFather("default", this);
        this.compareToPath = compareToPath;
        this.defaultBehavioural = defaultBehavioural;
    }
    public SwitchBT(Map<String, AbstractBehavioural> cases, String compareToPath) {
        super(cases);
        this.compareToPath = compareToPath;
        this.defaultBehavioural = null;
    }

    @Override
    public List<PacketField> asPacketFields() {
        Map<AbstractBehavioural, List<String>> cachedChildren = new LinkedHashMap<>();
        for (Map.Entry<String, AbstractBehavioural> entry : getChildren().entrySet()) {
            if (!entry.getKey().equals("default")) {
                cachedChildren
                        .computeIfAbsent(entry.getValue(), k -> new ArrayList<>()) // If the key doesn't exist, initialize a new ArrayList
                        .add(entry.getKey()); // Add the value (entry.getKey()) to the list
            }
        }
        List<Condition> negatedTotalConditions = new ArrayList<>();
        List<PacketField> fields = new ArrayList<>();
        UnsafeComponent nul =  new UnsafeComponent("null");

        for(Map.Entry<AbstractBehavioural, List<String>> entry : cachedChildren.entrySet()) {
            List<String> switchValues = entry.getValue();
            AbstractBehavioural bh = entry.getKey();
            List<PacketField> packetList = entry.getKey().asPacketFields().stream().map(p -> {
                RefComponent dRcomp = p.getSerializationInfo().getDeserializerRef().getComponent();
                RefComponent sRcomp = p.getSerializationInfo().getSerializerRef().getComponent();

                String compareToFinalName = compareToPath;
                List<Condition> conditions = new ArrayList<>();
                for(String s : switchValues) {
                    conditions.add(new EqualsComponent(compareToFinalName, s));
                //    if (!s.equals("default")) {
                        negatedTotalConditions.add(new EqualsComponent(compareToFinalName, s).negate());
                //    }
                }
                TernaryRefComponent newDeserializerRefComp = new TernaryRefComponent(conditions, dRcomp, nul, "||");
                TernaryRefComponent newSerializerRefComp = new TernaryRefComponent(conditions, sRcomp, nul, "||");

                SerializationInfo fieldInfo = new SerializationInfo(p.getSerializationInfo().getClassDescriptor(), new SerializerRef(newSerializerRefComp), new DeserializerRef(newDeserializerRefComp));
                return new PacketField(p.getName(), fieldInfo);
            }).toList();
            fields.addAll(packetList);
        }
        if(defaultBehavioural != null) {
            for (PacketField pf : defaultBehavioural.asPacketFields()) {
                RefComponent defaultDeserializerRefcomp = pf.getSerializationInfo().getDeserializerRef().getComponent();
                RefComponent defaultSerializerRefcomp = pf.getSerializationInfo().getSerializerRef().getComponent();

                TernaryRefComponent newDeserializerRefComp = new TernaryRefComponent(negatedTotalConditions, defaultDeserializerRefcomp, nul, "&&");
                TernaryRefComponent newSerializerRefComp = new TernaryRefComponent(negatedTotalConditions, defaultSerializerRefcomp, nul, "&&");

                SerializationInfo fieldInfo = new SerializationInfo(pf.getSerializationInfo().getClassDescriptor(), new SerializerRef(newSerializerRefComp), new DeserializerRef(newDeserializerRefComp));
                fields.add(new PacketField(pf.getName(), fieldInfo));
            }
        }
        return fields;
    }
}
