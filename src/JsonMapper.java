import Behaviourals.AbstractBehavioural;
import Behaviourals.ContainerBT;
import Generic.UnexpectedJsonFormatException;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class JsonMapper {


    private final Map<String, Map<String, Map<String, AbstractBehavioural>>> packets = new LinkedHashMap<>();
    private final Map<String, AbstractBehavioural> types = new LinkedHashMap<>();
    {
        BehaviouralFactory.setKnownBehaviourals(types);
    }

    public Map<String, Map<String, Map<String, AbstractBehavioural>>> getPackets() {
        return packets;
    }
    private static Map<String, Set<AbstractBehavioural>> flattenPacketsSet(Map<String, Map<String, Map<String, AbstractBehavioural>>> packets) {
        Map<String, Set<AbstractBehavioural>> flatMap = new LinkedHashMap<>();

        // Traverse the nested structure
        for (Map.Entry<String, Map<String, Map<String, AbstractBehavioural>>> entry : packets.entrySet()) {
            String firstKey = entry.getKey();
            Map<String, Map<String, AbstractBehavioural>> secondMap = entry.getValue();
            for (Map.Entry<String, Map<String, AbstractBehavioural>> secondEntry : secondMap.entrySet()) {
                String secondKey = secondEntry.getKey();
                Map<String, AbstractBehavioural> thirdMap = secondEntry.getValue();
                for (Map.Entry<String, AbstractBehavioural> thirdEntry : thirdMap.entrySet()) {
                    String thirdKey = thirdEntry.getKey();
                    AbstractBehavioural value = thirdEntry.getValue();

                    // Put the value in the flat map
                    flatMap.putIfAbsent(thirdKey, new LinkedHashSet<>());
                    flatMap.get(thirdKey).add(value);
                }
            }
        }

        return flatMap;
    }
    public Set<AbstractBehavioural> getPacketSet(String name){
        return flattenPacketsSet(packets).get(name);
    }
    public Map<String,Set<AbstractBehavioural>> getPacketSet(){
        return flattenPacketsSet(packets);
    }
    private static Map<String, AbstractBehavioural> flattenPackets(Map<String, Map<String, Map<String, AbstractBehavioural>>> packets) {
        Map<String, AbstractBehavioural> flatMap = new LinkedHashMap<>();

        // Traverse the nested structure
        for (Map.Entry<String, Map<String, Map<String, AbstractBehavioural>>> entry : packets.entrySet()) {
            String firstKey = entry.getKey();
            Map<String, Map<String, AbstractBehavioural>> secondMap = entry.getValue();

            for (Map.Entry<String, Map<String, AbstractBehavioural>> secondEntry : secondMap.entrySet()) {
                String secondKey = secondEntry.getKey();
                Map<String, AbstractBehavioural> thirdMap = secondEntry.getValue();

                for (Map.Entry<String, AbstractBehavioural> thirdEntry : thirdMap.entrySet()) {
                    String thirdKey = thirdEntry.getKey();
                    AbstractBehavioural value = thirdEntry.getValue();

                    // Create the flat key
                    String flatKey = thirdKey;
                    String finalKey = flatKey;

                    // Handle key collision with suffixes
                    int counter = 0;
                    while (flatMap.containsKey(finalKey)) {
                        finalKey = flatKey + counter;
                        counter++;
                    }

                    // Put the value in the flat map
                    flatMap.put(finalKey, value);
                }
            }
        }

        return flatMap;
    }
    public AbstractBehavioural getPacket(String name){
        return flattenPackets(packets).get(name);
    }

    private boolean isBehavioural(String s){
        try {
            BehaviouralFactory.valueOf(s.toUpperCase());
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Map<String, AbstractBehavioural> getTypes() {
        return types;
    }

    @JsonProperty("types")
    public void SetTypes(Map<String, Object> types){
        for(Map.Entry<String, Object> entry: types.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(!isBehavioural(key)) {
                AbstractBehavioural bh;
                if (value.equals("native")) {
                    bh = BehaviouralFactory.createBehavioural(entry.getKey());
                } else {
                    bh = BehaviouralFactory.createBehavioural(entry.getValue());
                    BehaviouralFactory.addKnownBehavioural(entry.getKey(), bh);
                    if(bh instanceof ContainerBT cbt){
                        cbt.disableDirectoryLevel();
                    }
                }
           //     BehaviouralFactory.addKnownBehavioural(entry.getKey(), bh);
            }
        }
    }
    @JsonAnySetter
    public void SetPacket(String state, Object value){
        packets.computeIfAbsent(state, k -> new LinkedHashMap<>());
        if (!(value instanceof Map<?, ?> targets)) {
            throw new UnexpectedJsonFormatException(value.toString());
        }

        // Process subcategories (e.g., toClient / toPacket)
        targets.forEach((target, packetListObj) -> {
            packets.get(state).computeIfAbsent(target.toString(), k -> new LinkedHashMap<>());

            if (!(packetListObj instanceof Map<?, ?> state_target_map)) {
                throw new RuntimeException("Unsupported format for packet: " + packetListObj);
            }


            Map<String, Object> localPacketsMap = (Map<String, Object>) state_target_map.get("types");
            for(Map.Entry<String, Object> node: localPacketsMap.entrySet()) {
                String packetName = node.getKey();
                Object jsonType = node.getValue();
                AbstractBehavioural bhType = BehaviouralFactory.createBehavioural(jsonType);
                if (packetName.startsWith("packet_")) {
                    packets.get(state).computeIfAbsent(packetName, k -> new LinkedHashMap<>());
                    packets.get(state).get(target).put(packetName, bhType);
                } else {
                    types.put(packetName, bhType);
                    if(bhType instanceof ContainerBT cbt){
                        cbt.disableDirectoryLevel();
                    }
                }
            }
        });
    }

}
