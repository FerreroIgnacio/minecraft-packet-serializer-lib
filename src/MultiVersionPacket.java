import Serialization.PacketField;

import java.util.*;

public class MultiVersionPacket {
    private final Map<String, Set<PacketField>> versionFields = new LinkedHashMap<>();
    private final Set<PacketField> globalFields;
    private final String name;

    public MultiVersionPacket(String name, String version, Set<PacketField> fields) {
        this.name = name;
        globalFields = new LinkedHashSet<>(fields);
        addVersion(version, fields);
    }

    public void addVersion(String version, Set<PacketField> field) {
        if(versionFields.containsKey(version)) {
           // throw new RuntimeException("Attempting to override version");
            return;
        }
        if(!field.isEmpty()) {
            versionFields.put(version, field);
        } else {
            int a;
        }

        //filter fields which are not present on a version;
        for (Iterator<PacketField> it = globalFields.iterator(); it.hasNext(); ) {
            PacketField pf = it.next();   // read current element
            if (!field.contains(pf)) {
                it.remove();// deletes pf from globalFields
            }
        }
        return;
    }

    public String getName() {
        return name;
    }

    public Set<PacketField> getGlobalFields() {
        return globalFields;
    }

    public Map<String, Set<PacketField>> getVersionFields() {
        return versionFields;
    }

    //returns a Map<PacketField, Set<String>>,  where that is the set of version where its present
    public Map<PacketField, Set<String>> getTotalCollapsedFields() {
        Map<String, Set<PacketField>> filteredFields = getVersionFields();

        Map<PacketField, Set<String>> totalFields = new LinkedHashMap<>();

        for(Map.Entry<String, Set<PacketField>> entry : filteredFields.entrySet()) {
            for(PacketField field : entry.getValue()) {
                totalFields.computeIfAbsent(field, k -> new LinkedHashSet<>()).add(entry.getKey());
            }
        }
        return totalFields;
    }
    public String generateFieldDeclarations(){
        StringBuilder fields = new StringBuilder();

        Set<PacketField> globalFields = getGlobalFields();
        for( Map.Entry<PacketField, Set<String>> entry: getTotalCollapsedFields().entrySet()){
            if (!globalFields.contains(entry.getKey())) {
                String versionComment = "//Only present on versions " + entry.getValue() + "\n";
                fields.append(versionComment);
            }
            String line = "private final " + entry.getKey().simpleRef() + ";\n";
            fields.append(line);
        }
        return fields.toString();
    }

    public String generateConstructors(){
        StringBuilder result = new StringBuilder();
        for(Map.Entry<String, Set<PacketField>> e : getVersionFields().entrySet()) {
            StringBuilder constructor = new StringBuilder("//Constructor for " + e.getKey()  +"\n@Version(\"" + e.getKey() + "\")\npublic " + getName() + "(");

            String superCall = "super(1,\"" + getName() +"\",\"" + e.getKey() +"\", -1)";
            List<String> constructorFields = new ArrayList<>();
            List<String> assignments = new ArrayList<>(List.of(superCall));
            for(PacketField pf : e.getValue()) {
                constructorFields.add(pf.simpleRef());
                assignments.add("this." + pf.getName() + " = " + pf.getName());
            }
            Set<PacketField> fieldsToNull = getTotalCollapsedFields().keySet();
            fieldsToNull.removeAll(e.getValue());
            for(PacketField pf : fieldsToNull ) {
                assignments.add("this." + pf.getName() + " = null;");
            }

            String assignmentsStr = String.join(";\n ", assignments);
            String simpleRefsStr = String.join(", ", constructorFields);
            constructor.append(simpleRefsStr + "){\n");
            constructor.append(assignmentsStr).append(";\n");
            constructor.append("}\n");
            result.append(constructor);
        }
        return result.toString();
    }

    public String generateGetters(){
        StringBuilder result = new StringBuilder();

    }

}
