package SerializationInfo;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClassDescriptor {
    private final Class<?> clazz;
    private final String className;
    private final List<ClassDescriptor> generics;
    private int arrayLevel;

    public ClassDescriptor(Class<?> clazz, List<ClassDescriptor> generics) {
        this.clazz = clazz;
        this.className = clazz.getSimpleName();
        this.generics = generics;
    }
    public ClassDescriptor(Class<?> clazz, List<ClassDescriptor> generics, int arrayLevel) {
        this.clazz = clazz;
        this.className = clazz.getSimpleName();
        this.generics = generics;
        this.arrayLevel = arrayLevel;
    }
    public ClassDescriptor(Class<?> clazz) {
        this(clazz, Collections.emptyList());
    }
    public ClassDescriptor(String className){
        this.clazz = null;
        this.className = className;
        this.generics = Collections.emptyList();
    }
    public ClassDescriptor(String className, List<ClassDescriptor> generics) {
        this.clazz = null;
        this.className = className;
        this.generics = generics;
    }

    public ClassDescriptor(String className, List<ClassDescriptor> generics, int arrayLevel) {
        this.clazz = null;
        this.className = className;
        this.generics = generics;
        this.arrayLevel = arrayLevel;
    }


    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        return className + (generics.isEmpty() ? "" : "<" + String.join(", ", generics.stream().map(ClassDescriptor::toString).toList()) + ">") + "[]".repeat(arrayLevel);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClassDescriptor that = (ClassDescriptor) obj;
        if (clazz != null) {
            return clazz.equals(that.clazz) && generics.equals(that.generics) && (arrayLevel == that.getArrayLevel());
        } else {
            return className.equals(that.className) && generics.equals(that.generics) && (arrayLevel == that.getArrayLevel());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, generics, arrayLevel);
    }

    public void arraify() {
        arrayLevel++;
    }
    public ClassDescriptor copy(){
        if(clazz == null)
            return new ClassDescriptor(className, generics, arrayLevel);
        return new ClassDescriptor(clazz, generics, arrayLevel);
    }

    public int getArrayLevel() {
        return arrayLevel;
    }
}

