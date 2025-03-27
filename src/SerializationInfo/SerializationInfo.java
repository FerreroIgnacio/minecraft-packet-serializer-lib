package SerializationInfo;

import SerializationInfo.Refs.DeserializerRef;
import SerializationInfo.Refs.SerializerRef;

public class SerializationInfo {
    private final ClassDescriptor classDescriptor;
    private final SerializerRef serializerRef;
    private final DeserializerRef deserializerRef;

    public SerializationInfo(Class<?> clazz, SerializerRef serializerRef, DeserializerRef deserializerRef) {
        this.classDescriptor = new ClassDescriptor(clazz);
        this.serializerRef = serializerRef;
        this.deserializerRef = deserializerRef;
    }
    public SerializationInfo(ClassDescriptor classDescriptor, SerializerRef serializerRef, DeserializerRef deserializerRef) {
        this.classDescriptor = classDescriptor;
        this.serializerRef = serializerRef;
        this.deserializerRef = deserializerRef;
    }

    public ClassDescriptor getClassDescriptor() {
        return classDescriptor;
    }

    public SerializerRef getSerializerRef() {
        return serializerRef;
    }

    public DeserializerRef getDeserializerRef() {
        return deserializerRef;
    }
}
