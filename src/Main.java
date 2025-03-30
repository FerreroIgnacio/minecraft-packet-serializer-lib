import Generic.GenericPath;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Hello, World!");
        String pcOrBedrock = "Pc";
        String version = "1.21.1";
        ObjectMapper aux0 = new ObjectMapper();
        final String dataPath = "minecraft-data/data/";
        File dataPaths = new File(dataPath + "dataPaths.json");

        Map<String, File> protocols = new LinkedHashMap<>();
        Map<String, Map<String, Map<String,String>>> versionsData = aux0.readValue(dataPaths, Map.class);
        for(Map<String, Map<String, String>> dist : versionsData.values()) {
            for(Map.Entry<String, Map<String,String>> versionNode : dist.entrySet()) {
                if(!versionNode.getKey().equals("0.30c")) {
                    if(versionNode.getValue().get("protocol") != null) {
                        String versionPath = versionNode.getValue().get("protocol");
                        File protocol = new File(dataPath + versionPath + "/protocol.json");
                        protocols.put(versionPath.substring(versionPath.indexOf("/") + 1), protocol);
                    }
                }
            }
        }
        Map<String, JsonMapper> jsonMappers = new LinkedHashMap<>();
        for(Map.Entry<String,File> entry: protocols.entrySet()) {
            try {
                JsonMapper aux = aux0.readValue(entry.getValue(), JsonMapper.class);
                jsonMappers.put(entry.getKey(), aux);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        JsonMapper aux = aux0.readValue(new File("minecraft-data/data/" + pcOrBedrock + "/" + version + "/protocol.json"), JsonMapper.class);
        int i = 5;
       /* JsonMapper.getPacket("packet_boss_bar").getChildren().get("title").asPacketFields();
        BehaviouralFactory.knownBehaviourals.get("command_node").getChildren().get("redirectNode").resolvePath(new GenericPath("flags"));

        BehaviouralFactory.knownBehaviourals.get("command_node").getChildren().get("redirectNode").resolvePath(new GenericPath("flags/has_redirect_node"));
        BehaviouralFactory.knownBehaviourals.get("command_node").asPacketFields();
        var aux2 = BehaviouralFactory.knownBehaviourals.get("Slot").get(null).asPacketFields();
        System.out.println(aux2);*/
    }

}