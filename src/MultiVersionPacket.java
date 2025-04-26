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
        versionFields.put(version, field);

        //filter fields which are not present on a version;
        for (Iterator<PacketField> it = globalFields.iterator(); it.hasNext(); ) {
            PacketField pf = it.next();   // read current element
            if (!field.contains(pf)) {
                it.remove();              // deletes pf from globalFields
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
    public Map<String, Set<PacketField>> getVersionedFields() {
        Map<String, Set<PacketField>> filteredFields = new LinkedHashMap<>();
        for(Map.Entry<String, Set<PacketField>> entry : versionFields.entrySet()) {
                Set<PacketField> filteredFieldSet = new LinkedHashSet<>();
                for(PacketField pf : entry.getValue()) {
            //        if(!globalFields.contains(pf)){
                        filteredFieldSet.add(pf);
          //          }
                }
                if(!filteredFieldSet.isEmpty())
                    filteredFields.put(entry.getKey(), filteredFieldSet);
        }
        return filteredFields;
    }
    public Set<PacketField> getTotalFields() {
        Map<String, Set<PacketField>> filteredFields = getVersionedFields();
        Set<PacketField> totalFields = new LinkedHashSet<>(getGlobalFields());
        for(Map.Entry<String, Set<PacketField>> entry : filteredFields.entrySet()) {
            totalFields.addAll(entry.getValue());
        }
        return totalFields;
    }
}
