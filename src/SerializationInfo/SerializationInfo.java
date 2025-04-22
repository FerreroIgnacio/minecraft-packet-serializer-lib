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
    public SerializationInfo(String className, SerializerRef serializerRef, DeserializerRef deserializerRef) {
        this.classDescriptor = new ClassDescriptor(className);
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

    @Override
    public String toString() {
        return classDescriptor + " #" + serializerRef + " #" + deserializerRef;
    }
    public SerializationInfo copy(){
        return new SerializationInfo(classDescriptor.copy(), serializerRef, deserializerRef);
    }
}
