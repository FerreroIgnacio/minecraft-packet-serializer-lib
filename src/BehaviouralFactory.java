import Behaviourals.*;
import SerializationInfo.Refs.Components.UnsafeComponent;
import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;
import SerializationInfo.SerializationInfo;

import java.awt.*;
import java.util.*;
import java.util.List;

public enum BehaviouralFactory {
    BITFIELD("bitfield"){
        @Override
        protected BehaviouralType build(Map<String, Object> map) {
            Map<String, Integer> sizes = new LinkedHashMap<>();
            Map<String, Boolean> signed = new LinkedHashMap<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                Map<String,Object> node = (Map<String, Object>) entry.getValue();
                sizes.put(entry.getKey(), (Integer)node.get("size"));
                signed.put(entry.getKey(), (Boolean)node.get("signed"));
            }
            return new BitfieldBT(sizes, signed);
        }
    },
    CONTAINER("container"){
        @Override
        protected BehaviouralType build(Map<String, Object> map) {
            Map<String, BehaviouralType> children = new HashMap<>();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                BehaviouralType child = createBehavioural(entry.getValue());
                children.put(entry.getKey(), child);
            }
            return new ContainerBT(children);
        }
    };


    private final static Map<String, BehaviouralType> knownBehaviourals = new LinkedHashMap<>();
    static {
        for(Natives n : Natives.values()) {
      //      knownBehaviourals.put(n.getNameInJson(), new ClassBT(n.getSerializationInfo()));
        }
    }

    private final String jsonName;
    BehaviouralFactory(String jsonName) {
        this.jsonName = jsonName;
    }

    public String getJsonName() {
        return jsonName;
    }

    public static void addKnownBehavioural(String key, BehaviouralType behavioural) {
        knownBehaviourals.put(key, behavioural);
    }

    protected BehaviouralType build(Map<String, Object> map){
        throw new RuntimeException("Attempting to create " + this
                + " from map");
    }

    public static BehaviouralType createBehavioural(Object o) {
        switch(o){
            case List<?> l: {
                String s = (String)l.getFirst();
                Object typeObject = l.getLast();
                try{
                    BehaviouralFactory factory = BehaviouralFactory.valueOf(s);
                    return factory.createBehavioural(typeObject);
                } catch(IllegalArgumentException e){
                    return new BehaviouralType() {
                        @Override
                        public List<PacketField> asPacketFields() {
                            return List.of();
                        }

                        @Override
                        public String toString() {
                            return "Unknown behavioural: " + s;
                        }
                    };
                }
            }
            case Map<?, ?> l: {
                return createBehavioural(((Map<String, Object>) o).get("type"));
            }
            case String s: {

                try {
                    Natives n = Natives.valueOf(s.toUpperCase());
                    return new ClassBT(n.getSerializationInfo());
                } catch (IllegalArgumentException e) {
                    if (knownBehaviourals.get(s) != null) {
                        return knownBehaviourals.get(s);
                    }
                    SerializerRef serRef = new SerializerRef(new UnsafeComponent("unknown type(serializer) " + s));
                    DeserializerRef deserRef = new DeserializerRef(new UnsafeComponent("unknown typedserializer) " + s));
                    return new ClassBT(new SerializationInfo(Object.class, serRef, deserRef));
                }
            }
            default: throw new RuntimeException("Unknown Json Type");
        }
    }
}
