import Generic.Consts;
import Serialization.PacketField;
import SerializationInfo.ClassDescriptor;

import java.util.*;
import java.util.stream.Collectors;

public class MultiVersionPacket {
    private final Map<String, LinkedHashSet<PacketField>> versionFields = new LinkedHashMap<>();
    private final Set<PacketField> globalFields;
    private final String name;

    //Map of style name -> map<version, finalName>, for example
    // position -> (1.12 -> positionXYZ)
    /*
    private static Map<String, Map<String, String>> versionedFinalNames;

    public static Map<String, Map<String, String>> getVersionedFinalNames() {
        return versionedFinalNames;
    }

    public static void setVersionedFinalNames(Map<String, Map<String, String>> versionedFinalNames) {
        MultiVersionPacket.versionedFinalNames = versionedFinalNames;
    }*/

    public MultiVersionPacket(String name, String version, Set<PacketField> fields) {
        this.name = name;
        globalFields = new LinkedHashSet<>(fields);
        addVersion(version, new LinkedHashSet<>(fields));  // Create a copy to avoid shared references
    }
    private final Set<String> usedRenames = new HashSet<>();
    public void addVersion(String version, Set<PacketField> newFields) {
        if (versionFields.containsKey(version)) {
            return;
        }

        // Filter fields which are not present on a version
        // Use proper content-based comparison rather than identity
        globalFields.removeIf(globalField -> newFields.stream().noneMatch(field -> field.equals(globalField)));

        final List<PacketField> everyFieldInstance = getVersionFields().values().stream().flatMap(Collection::stream).toList();
        Map<String, PacketField> nameToFieldMap = new LinkedHashMap<>();
        for(PacketField f : everyFieldInstance) {
            nameToFieldMap.put(f.getName(), f);
        }
        final Set<String> everyFieldName = everyFieldInstance.stream().map(PacketField::getName).collect(Collectors.toSet());

        final List<PacketField> fieldsToRename = new ArrayList<>();
        final Set<String> namesToRename = new LinkedHashSet<>();
        for(PacketField field : newFields) {
            if(!globalFields.contains(field)){
                if(usedRenames.contains(field.getName())){
                    fieldsToRename.add(field);
                    namesToRename.add(field.getName());
                }
                if(everyFieldName.contains(field.getName()) && !nameToFieldMap.get(field.getName()).equals(field)){
                    usedRenames.add(field.getName());
                    fieldsToRename.add(nameToFieldMap.get(field.getName()));
                }
            }
        }

        for(PacketField p : fieldsToRename) {
                p.setName(p.getName() + "_" + p.getSerializationInfo().getClassDescriptor().getClassName());
        }

        versionFields.put(version, new LinkedHashSet<>(newFields));
    }
    public String getName() {
        return name;
    }

    public Set<PacketField> getGlobalFields() {
        return globalFields;
    }

    public Map<String, LinkedHashSet<PacketField>> getVersionFields() {
        return versionFields;
    }

    //returns a Map<PacketField, Set<String>>,  where that is the set of version where its present
    public Map<PacketField, Set<String>> getPerFieldVersions() {
        Map<String, LinkedHashSet<PacketField>> filteredFields = getVersionFields();
        Map<PacketField, Set<String>> totalFields = new LinkedHashMap<>();
        for(Map.Entry<String, LinkedHashSet<PacketField>> entry : filteredFields.entrySet()) {
            for(PacketField field : entry.getValue()) {
                totalFields.computeIfAbsent(field, k -> new LinkedHashSet<>()).add(entry.getKey());
            }
        }
        return totalFields;
    }

    private static class pseudoField {
        String name;
        ClassDescriptor classDescriptor;
        boolean global;

        public pseudoField(String name, ClassDescriptor classDescriptor, boolean global) {
            this.name = name;
            this.classDescriptor = classDescriptor;
            this.global = global;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            pseudoField uniqueKey = (pseudoField) o;
            return global == uniqueKey.global && Objects.equals(name, uniqueKey.name) && Objects.equals(classDescriptor, uniqueKey.classDescriptor);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, classDescriptor, global);
        }

        @Override
        public String toString() {
            return name + " " + classDescriptor + " " + global;
        }
    }
    private static String getTypeSuffix(ClassDescriptor type) {
        // Add array depth and type name for uniqueness
        StringBuilder sb = new StringBuilder();
        int arrayDepth = 0;
        String className = type.getClassName();
        while (className.endsWith("[]")) {
            arrayDepth++;
            className = className.substring(0, className.length() - 2);
        }
        if (arrayDepth > 0) {
            sb.append("_arr").append(arrayDepth);
        }
        sb.append("_").append(className.replace('.', '_'));
        return sb.toString();
    }

    //Map<uniqueKey, Versiones>
    private Map<pseudoField, Set<String>> getCollapsedFields() {
        Map<pseudoField, Set<String>> collapsedFields = new LinkedHashMap<>();
        Map<String, Set<ClassDescriptor>> nameToTypes = new LinkedHashMap<>();
        int anonCounter = 1;
        Map<PacketField, String> fieldToName = new LinkedHashMap<>();
        // First pass: collect all types for each field name, assign anon names
        for (Map.Entry<PacketField, Set<String>> entry : getPerFieldVersions().entrySet()) {
            String baseName = entry.getKey().getName();
            ClassDescriptor type = entry.getKey().getSerializationInfo().getClassDescriptor();
            if (baseName == null || baseName.isBlank()) {
                // Special case: if this is a command node array or similar, use "nodes"
                String typeName = type.getClassName().toLowerCase();
                if (typeName.contains("command_node") || typeName.contains("nodes")) {
                    baseName = "nodes";
                } else {
                    baseName = "anonField" + anonCounter++;
                }
            }
            fieldToName.put(entry.getKey(), baseName);
            nameToTypes.computeIfAbsent(baseName, k -> new LinkedHashSet<>()).add(type);
        }
        // Second pass: assign names, only suffix if type conflict
        anonCounter = 1;
        for (Map.Entry<PacketField, Set<String>> entry : getPerFieldVersions().entrySet()) {
            PacketField pf = entry.getKey();
            String baseName = fieldToName.get(pf);
            ClassDescriptor type = pf.getSerializationInfo().getClassDescriptor();
            boolean isGlobal = globalFields.contains(pf);
            String uniqueName = baseName;
            if (nameToTypes.get(baseName).size() > 1) {
                uniqueName = baseName + getTypeSuffix(type);
            }
            pseudoField key = new pseudoField(uniqueName, type, isGlobal);
            collapsedFields.computeIfAbsent(key, k -> new LinkedHashSet<>()).addAll(entry.getValue());
        }
        return collapsedFields;
    }
    public Map<Set<PacketField>, Set<String>> getSetCollapsedFields() {
        Map<Set<PacketField>, Set<String>> collapsedConstructors = new LinkedHashMap<>();
        for(Map.Entry<String, LinkedHashSet<PacketField>> e : getVersionFields().entrySet()) {
            // Convert to a more consistent collection type for key usage
            Set<PacketField> keySet = new HashSet<>(e.getValue());
            collapsedConstructors.computeIfAbsent(keySet, k -> new LinkedHashSet<>()).add(e.getKey());
        }
        return collapsedConstructors;
    }
    public String generateFieldDeclarations(){
        StringBuilder fields = new StringBuilder();
        for(Map.Entry<pseudoField, Set<String>> entry: getCollapsedFields().entrySet()){
            pseudoField key = entry.getKey();
            Set<String> versions = entry.getValue();
            if (key.name == null || key.name.isBlank()) {
                // Should not happen now, but skip just in case
                continue;
            }
            if(!key.global) {
                   fields.append("//Only present on versions " + entry.getValue() + "\n");
            }
            fields.append("private final " + entry.getKey().classDescriptor.resolveToString(versions.iterator().next()) + " " + entry.getKey().name + ";\n");
        }
        return fields.toString();
    }
    public static String capitalizeFirst(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
    public static String setStrFromSet(Set<?> set) {
        String p = set.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", "));
        return "Set.of(" + p + ")";
    }
    public String generateConstructors(){
        StringBuilder result = new StringBuilder();
        Map<Set<pseudoField>, LinkedHashSet<String>> collapsedConstructors = new LinkedHashMap<>();
        Map<String, pseudoField> fieldNameToPseudo = new LinkedHashMap<>();
        Map<String, Set<ClassDescriptor>> nameToTypes = new LinkedHashMap<>();
        int anonCounter = 1;
        for (Map.Entry<PacketField, Set<String>> entry : getPerFieldVersions().entrySet()) {
            String baseName = entry.getKey().getName();
            if (baseName == null || baseName.isBlank()) {
                baseName = "anonField" + anonCounter++;
            }
            ClassDescriptor type = entry.getKey().getSerializationInfo().getClassDescriptor();
            nameToTypes.computeIfAbsent(baseName, k -> new LinkedHashSet<>()).add(type);
        }
        for (Map.Entry<pseudoField, Set<String>> entry : getCollapsedFields().entrySet()) {
            fieldNameToPseudo.put(entry.getKey().name, entry.getKey());
        }
        for(Map.Entry<String, LinkedHashSet<PacketField>> e : getVersionFields().entrySet()) {
            final int[] anonCounterArr = {1};
            Set<pseudoField> keySet = new LinkedHashSet<>(e.getValue().stream().map(pf -> {
                String baseName = pf.getName();
                if (baseName == null || baseName.isBlank()) {
                    baseName = "anonField" + anonCounterArr[0]++;
                }
                ClassDescriptor type = pf.getSerializationInfo().getClassDescriptor();
                String uniqueName = baseName;
                if (nameToTypes.get(baseName).size() > 1) {
                    uniqueName = baseName + getTypeSuffix(type);
                }
                for (pseudoField pfld : getCollapsedFields().keySet()) {
                    if (pfld.classDescriptor.equals(type) && pfld.name.equals(uniqueName)) {
                        return pfld;
                    }
                }
                return new pseudoField(uniqueName, type, globalFields.contains(pf));
            }).filter(pf -> pf.name != null && !pf.name.isBlank())
            .collect(Collectors.toCollection(LinkedHashSet::new)));
            collapsedConstructors.computeIfAbsent(keySet, k -> new LinkedHashSet<>()).add(e.getKey());
        }

        List<String> versionSets = new ArrayList<>();
        int i = 0;
        Set<String> seenSignatures = new HashSet<>();
        for(Map.Entry<Set<pseudoField>, LinkedHashSet<String>> e : collapsedConstructors.entrySet()) {
            StringBuilder constructor = new StringBuilder(
                "@Version({" + e.getValue().stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")) + "})\n" +
                "public " + getName() + "(");
            String superCall = "super(-1,\"" + getName() +"\"   ,CONSTRUCTOR"+  i + ", -1)";
            versionSets.add(setStrFromSet(e.getValue()));
            List<String> constructorFields = new ArrayList<>();
            List<String> assignments = new ArrayList<>(List.of(superCall));
            Set<pseudoField> fieldsToNull = new LinkedHashSet<>(getCollapsedFields().keySet());

            int unnamedCounter = 1;
            Map<pseudoField, String> paramNames = new LinkedHashMap<>();
            List<String> paramTypes = new ArrayList<>();
            for(pseudoField uk : e.getKey()) {
                if (uk.name == null || uk.name.isBlank()) {
                    // Skip unnamed fields
                    continue;
                }
                String paramName = uk.name;
                paramNames.put(uk, paramName);
                String paramType = uk.classDescriptor.resolveToString(e.getValue().getFirst());
                constructorFields.add(paramType + " " + paramName);
                paramTypes.add(paramType);
                assignments.add("this." + uk.name + " = " + paramName);
                fieldsToNull.remove(uk);
            }
            for(pseudoField key : fieldsToNull ) {
                if (key.name == null || key.name.isBlank()) {
                    continue;
                }
                assignments.add("this." + key.name + " = null");
            }
            // Ensure unique constructor signature
            String signature = String.join(",", paramTypes);
            if (seenSignatures.contains(signature)) {
                constructorFields.add("int versionMarker");
                assignments.add("// versionMarker is a dummy parameter to ensure unique signature");
            }
            seenSignatures.add(signature);
            String assignmentsStr = String.join(";\n ", assignments);
            String simpleRefsStr = String.join(", ", constructorFields);
            constructor.append(simpleRefsStr + "){\n");
            constructor.append(assignmentsStr).append(";\n");
            constructor.append("}\n");
            result.append(constructor);
            i++;
        }
        StringBuilder staticSetDeclarations = new StringBuilder();
        for(int j = 0; j < versionSets.size(); j++) {
            staticSetDeclarations.append("private final static Set<String> CONSTRUCTOR" + j + " = " + versionSets.get(j) + ";\n");
        }
        return staticSetDeclarations.toString() + result.toString();
    }

    private final static String GETVERSION = "getVersion()";

    public String generateGetters(){
        StringBuilder result = new StringBuilder();
        for(Map.Entry<pseudoField, Set<String>> entry : getCollapsedFields().entrySet()) {
            pseudoField key = entry.getKey();
            Set<String> versions = entry.getValue();
            String firstLine = "public " + key.classDescriptor.resolveToString(versions.iterator().next()) + " get" + capitalizeFirst(key.name) + "()";
            if(!key.global) {
                firstLine += " throws VersionSpecificException ";
            }
            firstLine += "{\n";
            StringBuilder exCondition = new StringBuilder();

            exCondition.append("!" + GETVERSION + ".containsAll(" + setStrFromSet(entry.getValue())  + ")");
            String exStr = "if(" + exCondition.toString() + "){\n" +
                    "throw new VersionSpecificException(" + GETVERSION + ", new String[]{" + versions.stream()
                    .map(s -> "\"" + s + "\"")
                    .collect(Collectors.joining(", ")) + "});\n" +
                    "}\n";
            String getLine = "return " + key.name + ";\n";
            result.append(firstLine);
            if(!key.global) {
                result.append(exStr);
            }
            result.append(getLine);
            result.append("}\n");
        }
        return result.toString();
    }


    public String generateRead(){
        StringBuilder read = new StringBuilder("public static " + getName() + " read(ByteBuffer " + Consts.BUFNAME + ", String version){\n");
        read.append("switch(version){\n");
        for(Map.Entry<String, LinkedHashSet<PacketField>> e : getVersionFields().entrySet()) {
            StringBuilder aux = new StringBuilder("\"" + e.getKey() + "\" : {\n");
            for (PacketField pf : e.getValue()) {
                aux.append(pf.toString() + ";\n");
            }
            aux.append("return new " + getName() + "(" + String.join(",", e.getValue().stream().map(PacketField::getName).toList()) +");");
            aux.append("\n}\n");

            read.append(aux);
        }

        read.append("}\nthrow new RuntimeException(\"Unsupported version\");\n}\n");
        return read.toString();
    }

    // Returns all PacketFields across all versions (union)
    public Set<PacketField> getAllFields() {
        Set<PacketField> allFields = new LinkedHashSet<>();
        for (Set<PacketField> fields : versionFields.values()) {
            allFields.addAll(fields);
        }
        return allFields;
    }
}
