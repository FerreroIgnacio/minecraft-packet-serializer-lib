package SerializationInfo;

import Behaviourals.AbstractBehavioural;

import java.util.*;

public class ClassDescriptor {
    private final Class<?> clazz;
    private final String className;
    private final List<ClassDescriptor> generics;
    private int arrayLevel;

    //Map in style of position -> (1.12 -> positionXYZ), one for each version
    public static Map<String, Map<String, String>> resolveMap;

    public static Map<String, Map<String,String>> getResolveMap() {
        return resolveMap;
    }

    public static void setResolveMap(Map<String, Map<String, String>> resolveMap) {
        ClassDescriptor.resolveMap = resolveMap;
    }

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
    public String resolveToString(String version){
        if(clazz != null){
            return toString();
        }
        Map<String, String> typeVariations = resolveMap.get(className.toLowerCase());
        if(typeVariations == null){
            throw new RuntimeException("Attempting to resolve non existent type " + className);
        }
        String finalName = typeVariations.get(version);
       // return finalName;
       return finalName + (generics.isEmpty() ? "" : "<" + String.join(", ", generics.stream().map(ClassDescriptor::toString).toList()) + ">") + "[]".repeat(arrayLevel);
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

    public List<ClassDescriptor> getGenerics() {
        return generics;
    }

    public String getClassName() {
        return className;
    }

    // Returns true if this type is a Java primitive or known native type
    public boolean isNative() {
        if (clazz != null) {
            return clazz.isPrimitive() || clazz.getPackageName().startsWith("java.");
        }
        // Add more logic if you have custom native types
        return false;
    }
    // Returns the class name (for compatibility with getName() usage)
    public String getName() {
        return getClassName();
    }
}
