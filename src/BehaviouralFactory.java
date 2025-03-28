import Behaviourals.*;
import Generic.UnexpectedJsonFormatException;
import Serialization.Natives;
import Serialization.PacketField;
import SerializationInfo.Refs.Components.UnsafeComponent;
import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;
import SerializationInfo.SerializationInfo;

import java.util.*;
import java.util.List;

public enum BehaviouralFactory {
    BITFIELD("bitfield"){
        @Override
        protected AbstractBehavioural build(List<Map<String, Object>> l) {

            LinkedHashMap<String, AbstractBehavioural> children = new LinkedHashMap<>();
            for(Map<String, Object> node : l) {
                int size = (Integer)node.get("size");
                boolean signed = (Boolean)node.get("signed");
                children.put((String)node.get("name"), new BitfieldComponentBT(size, signed));
            }
            return new BitfieldBT(children);
        }
    },
    CONTAINER("container"){
        @Override
        protected AbstractBehavioural build(List<Map<String, Object>> l) {
            LinkedHashMap<String, AbstractBehavioural> children = new LinkedHashMap<>();
            for(Map<String, Object> node : l ) {
                AbstractBehavioural child = createBehavioural(node.get("type"));
                children.put((String)node.get("name"), child);
            }
            return new ContainerBT(children);
        }
    },

    ;


    private final static Map<String, AbstractBehavioural> knownBehaviourals = new LinkedHashMap<>();
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

    public static void addKnownBehavioural(String key, AbstractBehavioural behavioural) {
        knownBehaviourals.put(key, behavioural);
    }

    protected AbstractBehavioural build(Map<String, Object> map){
        throw new RuntimeException("Attempting to create " + this
                + " from map");
    }
    protected AbstractBehavioural build(List<Map<String, Object>> l){
        throw new RuntimeException("Attempting to create " + this
                + " from list");
    }
    private AbstractBehavioural objectBuild(Object o){
        switch (o){
            case List<?> l: {
                return this.build((List<Map<String, Object>>)l);
            }
            case Map<?,?> m: {
                return this.build((Map<String, Object>)m);
            }
            case String s: {
                return createBehavioural(s);
            }
            default: {
                throw new UnexpectedJsonFormatException(o.toString());
            }
        }
    }

    public static AbstractBehavioural createBehavioural(Object o) {
        switch(o){
            case List<?> l: {
                String s = (String)l.getFirst();
                Object typeObject = l.getLast();
                try{
                    BehaviouralFactory factory = BehaviouralFactory.valueOf(s.toUpperCase());
                    return factory.objectBuild(typeObject);
                } catch(IllegalArgumentException e){
                    return new AbstractBehavioural() {
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
