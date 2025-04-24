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
    BITFLAGS("bitflags") {
        @Override
        protected AbstractBehavioural build(Map<String, Object> map) {

            ClassBT type = (ClassBT)BehaviouralFactory.createBehavioural(map.get("type"));
            List<Object> flags = (List<Object>)map.get("flags");
            int i = 1;
            LinkedHashMap<String, AbstractBehavioural> children = new LinkedHashMap<>();
            for(Object o : flags) {
                String flag = (String)o;
                children.put(flag, new BitflagsComponentBT(i));
                i++;
            }
            return new Bitflags(children, type);
        }
    },
    ARRAY("array"){
        @Override
        protected AbstractBehavioural build(Map<String, Object> map) {
            AbstractBehavioural type = BehaviouralFactory.createBehavioural(map.get("type"));
            Object countType = map.get("countType");
            if(countType == null){
                Object fixedCountOrPath = map.get("count");
                switch (fixedCountOrPath){
                    case Integer i: {
                        return new ArrayBT(i, type);
                    }
                    case String s: {
                        return new ArrayBT(s, type);
                    }
                    default: {
                        throw new UnexpectedJsonFormatException("Array count of unexpected type: " + fixedCountOrPath.getClass() + fixedCountOrPath);
                    }
                }
            }
            return new ArrayBT(BehaviouralFactory.createBehavioural(countType), type);
        }
    },
    MAPPER("mapper"){
      @Override
      protected AbstractBehavioural build(Map<String, Object> map) {
            ClassBT type = (ClassBT) BehaviouralFactory.createBehavioural(map.get("type"));
            Map<String, Object> mappings = (Map<String, Object>) map.get("mappings");
            Map<String, String> mapp = new LinkedHashMap<>();
            for(Map.Entry<String, Object> entry : mappings.entrySet()){
                mapp.put(entry.getKey(), entry.getValue().toString());
            }
            return new MapperBT(mapp, type);
      }
    },
    BUFFER("buffer"){
        protected AbstractBehavioural build(Map<String, Object> map) {
            if(map.get("countType") != null){
                ClassBT type = (ClassBT) BehaviouralFactory.createBehavioural(map.get("countType"));
                return new BufferBT(type);
            } else {
                switch(map.get("count")){
                    case Integer i: { return new BufferBT(i);}
                    case String s: { return new BufferBT(s);}
                    default: { throw new UnexpectedJsonFormatException("Count of unexpected type: " + map.get("count")); }
                }
            }
        }
    },
    OPTION("option"){
        protected AbstractBehavioural build(List<Map<String, Object>> l) {
            AbstractBehavioural type = BehaviouralFactory.createBehavioural(l);
            return new OptionBT(type);
        }
    },
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
                String key = (String)(node.get("name") == null ? "anon" : node.get("name"));
                children.put( key , child);
            }
            return new ContainerBT(children);
        }
    },
    SWITCH("switch"){
        @Override
        protected AbstractBehavioural build(Map<String, Object> map) {
            String compareToFieldName = (String) map.get("compareTo");
            LinkedHashMap<String, AbstractBehavioural> switchFields = new LinkedHashMap<>();

            LinkedHashMap<String, Object> fields = (LinkedHashMap<String, Object>) map.get("fields");
            for (Map.Entry<String, Object> node : fields.entrySet()) {
                switchFields.put(node.getKey(), BehaviouralFactory.createBehavioural(node.getValue()));
            }
            if (map.get("default") != null) {
                AbstractBehavioural defaultField = BehaviouralFactory.createBehavioural(map.get("default"));
                return new SwitchBT(switchFields, compareToFieldName, defaultField);
            } else {
                return new SwitchBT(switchFields, compareToFieldName);
            }
        }
    }

    ;


    public static Map<String, AbstractBehavioural> knownBehaviourals = new LinkedHashMap<>();
    public static void setKnownBehaviourals(Map<String, AbstractBehavioural> knownBehaviourals) {
        BehaviouralFactory.knownBehaviourals = knownBehaviourals;
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
                        public boolean isBuildable() {
                            return false;
                        }

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
                    return new ReferenceBT(s, knownBehaviourals.get(s));
                }
                    /*
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
                     */
            }
            default: throw new RuntimeException("Unknown Json Type");
        }
    }
}
