package SerializationInfo;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClassDescriptor {
    private final Class<?> clazz;
    private final List<ClassDescriptor> generics;
    private int arrayLevel;

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
        return clazz.getSimpleName() + (generics.isEmpty() ? "" : "<" + String.join(", ", generics.stream().map(ClassDescriptor::toString).toList()) + ">" + "[]".repeat(arrayLevel));
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClassDescriptor that = (ClassDescriptor) obj;
        return clazz.equals(that.clazz) && generics.equals(that.generics) && (arrayLevel == that.getArrayLevel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, generics, arrayLevel);
    }

    public void arraify() {
        arrayLevel++;
    }

    public int getArrayLevel() {
        return arrayLevel;
    }
}

