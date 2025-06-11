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
    public SwitchBT(LinkedHashMap<String, AbstractBehavioural> cases, String compareToPath, AbstractBehavioural defaultBehavioural) {
        super(cases, true);
        getChildren().put("default", defaultBehavioural);
        defaultBehavioural.setFather("default", this);
        this.compareToPath = compareToPath;
        this.defaultBehavioural = defaultBehavioural;
    }
    public SwitchBT(LinkedHashMap<String, AbstractBehavioural> cases, String compareToPath) {
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
        final String finalCompareToName;
        if(!compareToPath.startsWith("$")) {
             finalCompareToName = resolvePath(compareToPath).getName();
        } else {
            finalCompareToName =  "externalCompareTo" + compareToPath;
        }

        for(Map.Entry<AbstractBehavioural, List<String>> entry : cachedChildren.entrySet()) {
            List<String> switchValues = entry.getValue();
            for(String s : switchValues) {
                negatedTotalConditions.add(new EqualsComponent(finalCompareToName, s).negate());
            }
            AbstractBehavioural bh = entry.getKey();
            List<PacketField> packetList = entry.getKey().asPacketFields().stream().map(p -> {
                RefComponent dRcomp = p.getSerializationInfo().getDeserializerRef().getComponent();
                RefComponent sRcomp = p.getSerializationInfo().getSerializerRef().getComponent();

                List<Condition> conditions = new ArrayList<>();
                for(String s : switchValues) {
                    conditions.add(new EqualsComponent(finalCompareToName, s));
                }
                TernaryRefComponent newDeserializerRefComp = new TernaryRefComponent(conditions, dRcomp, nul, "||");
                TernaryRefComponent newSerializerRefComp = new TernaryRefComponent(conditions, sRcomp, nul, "||");

                SerializationInfo fieldInfo = new SerializationInfo(p.getSerializationInfo().getClassDescriptor(), new SerializerRef(newSerializerRefComp), new DeserializerRef(newDeserializerRefComp));
                return new PacketField(cachedChildren.size() > 1 ? (this.getName().isEmpty() ? "" : getName() + "_") + p.getName() : this.getName(), fieldInfo, List.of(this));
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
                fields.add(new PacketField(pf.getName(), fieldInfo, List.of(this)));
            }
        }
        return fields;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SwitchBT that = (SwitchBT) obj;
        if (defaultBehavioural != null) {
            if (!this.defaultBehavioural.equals(that.defaultBehavioural))
                return false;
        } else if (that.defaultBehavioural != null) {
            return false;
        }
        return this.compareToPath.equals(that.compareToPath)
                && getChildren().equals(that.getChildren());
    }
}
