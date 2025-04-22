import Behaviourals.AbstractBehavioural;
import Generic.GenericPath;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Hello, World!");
        String pcOrBedrock = "Pc";
        String version = "1.21.1";
        ObjectMapper mapper = new ObjectMapper();
        final String dataPath = "minecraft-data/data/";
        File dataPaths = new File(dataPath + "dataPaths.json");

        Map<String, File> protocolsFileMap = new LinkedHashMap<>();
        //read available version data
        Map<String, Map<String, Map<String,String>>> versionsData = mapper.readValue(dataPaths, Map.class);

        //scan directory
        for(Map<String, Map<String, String>> dist : versionsData.values()) {
            for(Map.Entry<String, Map<String,String>> versionNode : dist.entrySet()) {
                if(!versionNode.getKey().equals("0.30c")) {
                    if(versionNode.getValue().get("protocol") != null) {
                        String versionPath = versionNode.getValue().get("protocol");
                        File protocol = new File(dataPath + versionPath + "/protocol.json");
                        protocolsFileMap.put(versionPath.substring(versionPath.indexOf("/") + 1), protocol);
                    }
                }
            }
        }

        Map<String, JsonMapper> versionProtocols = new LinkedHashMap<>();
        Map<String, Set<AbstractBehavioural>> globalTypes = new LinkedHashMap<>();
       // Multimap<String, AbstractBehavioural> gTypes = ArrayListMultimap.create();

        for(Map.Entry<String,File> entry: protocolsFileMap.entrySet()) {
            try {
                JsonMapper versionMappedProtocol = mapper.readValue(entry.getValue(), JsonMapper.class);
                versionProtocols.put(entry.getKey(), versionMappedProtocol);


                for(Map.Entry<String, AbstractBehavioural> typeEntry: versionMappedProtocol.getTypes().entrySet()){
                    String typeName = typeEntry.getKey().toLowerCase();
                    AbstractBehavioural type = typeEntry.getValue();

                    if(!globalTypes.containsKey(typeName)) {
                        globalTypes.put(typeName, new LinkedHashSet<>(Set.of(type)));
                    } else {
                        globalTypes.get(typeName).add(type);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

      //  JsonMapper aux = mapper.readValue(new File("minecraft-data/data/" + pcOrBedrock + "/" + version + "/protocol.json"), JsonMapper.class);
       // int i = 5;
       /* JsonMapper.getPacket("packet_boss_bar").getChildren().get("title").asPacketFields();
        BehaviouralFactory.knownBehaviourals.get("command_node").getChildren().get("redirectNode").resolvePath(new GenericPath("flags"));

        BehaviouralFactory.knownBehaviourals.get("command_node").getChildren().get("redirectNode").resolvePath(new GenericPath("flags/has_redirect_node"));
        BehaviouralFactory.knownBehaviourals.get("command_node").asPacketFields();
        var aux2 = BehaviouralFactory.knownBehaviourals.get("Slot").get(null).asPacketFields();
        System.out.println(aux2);*/
    }

}