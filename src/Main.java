import Generic.GenericPath;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Hello, World!");
        String pcOrBedrock = "Pc";
        String version = "1.21.1";
        ObjectMapper aux0 = new ObjectMapper();
        JsonMapper aux = aux0.readValue(new File("minecraft-data/data/" + pcOrBedrock + "/" + version + "/protocol.json"), JsonMapper.class);
        int i = 5;
        JsonMapper.getPacket("packet_boss_bar").getChildren().get("title").asPacketFields();
        BehaviouralFactory.knownBehaviourals.get("command_node").getChildren().get("redirectNode").resolvePath(new GenericPath("flags"));

        BehaviouralFactory.knownBehaviourals.get("command_node").getChildren().get("redirectNode").resolvePath(new GenericPath("flags/has_redirect_node"));
        BehaviouralFactory.knownBehaviourals.get("command_node").asPacketFields();
    }

}