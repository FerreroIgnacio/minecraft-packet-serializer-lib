import Behaviourals.AbstractBehavioural;
import Generic.GenericPath;
import Serialization.PacketField;
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
                        if (versionPath.startsWith("pc")) {
                            File protocol = new File(dataPath + versionPath + "/protocol.json");
                            protocolsFileMap.put(versionPath.substring(versionPath.indexOf("/") + 1), protocol);
                        }
                    }
                }
            }
        }

        Map<String, JsonMapper> versionProtocols = new LinkedHashMap<>();
        Map<String, Set<AbstractBehavioural>> globalTypes = new LinkedHashMap<>();
        Map<String, Set<PacketField>> globalPackets = new LinkedHashMap<>();
        Map<String, Set<AbstractBehavioural>> globalPacketBack = new LinkedHashMap<>();

        Map<String, Set<Packet>> neoPackets = new LinkedHashMap<>();
        for(Map.Entry<String,File> entry: protocolsFileMap.entrySet()) {
            try {
                JsonMapper versionMappedProtocol = mapper.readValue(entry.getValue(), JsonMapper.class);
             //   versionMappedProtocol;
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
                for(Map.Entry<String, Set<AbstractBehavioural>> typeEntry: versionMappedProtocol.getPacketSet().entrySet()){
                    globalPacketBack.putIfAbsent(typeEntry.getKey(), new LinkedHashSet<>());
                    globalPacketBack.get(typeEntry.getKey()).addAll(typeEntry.getValue());

                    globalPackets.putIfAbsent(typeEntry.getKey(), new LinkedHashSet<>());
                    for(AbstractBehavioural packet : typeEntry.getValue()) {
                        globalPackets.get(typeEntry.getKey()).addAll(packet.asPacketFields());
                    }

                    neoPackets.putIfAbsent(typeEntry.getKey(), new LinkedHashSet<>());
                    for(AbstractBehavioural packet : typeEntry.getValue()) {
                        neoPackets.get(typeEntry.getKey()).add(new Packet(packet, typeEntry.getKey(), entry.getKey()));
                    }
                }
            } catch (Exception e) {
                 throw new RuntimeException(e);
            }
        }

        List<Map.Entry<String, AbstractBehavioural>> unbuildableTypes = new ArrayList<>();
        for(Map.Entry<String, Set<AbstractBehavioural>> entry : globalTypes.entrySet()) {
            for(AbstractBehavioural type : entry.getValue()) {
                if(!type.isBuildable()){
                    unbuildableTypes.add(new AbstractMap.SimpleEntry<>(entry.getKey(), type) {});
                }
            }
        }

    }

}