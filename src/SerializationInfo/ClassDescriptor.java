package SerializationInfo;

import java.util.Collections;
import java.util.List;

public class ClassDescriptor {
    private final Class<?> clazz;
    private final List<ClassDescriptor> generics;

    public ClassDescriptor(Class<?> clazz, List<ClassDescriptor> generics) {
        this.clazz = clazz;
        this.generics = generics;
    }
    public ClassDescriptor(Class<?> clazz) {
        this.clazz = clazz;
        this.generics = Collections.emptyList();
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        return clazz.getSimpleName() + (generics == null ? "" : "<" + String.join(", ", generics.stream().map(ClassDescriptor::toString).toList()) + ">");
    }
}

