import Behaviourals.AbstractBehavioural;
import Generic.Consts;
import Serialization.PacketField;
import SerializationInfo.ClassDescriptor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.math.BigInteger;
import java.util.stream.Collectors;

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
        Map<String, Set<PacketField>> globalPackets = new LinkedHashMap<>();
        Map<String, Set<AbstractBehavioural>> globalPacketBack = new LinkedHashMap<>();

        //Sets neoPackets and globalTypes
        Map<String, Map<String, AbstractBehavioural>> globalTypes = new LinkedHashMap<>();

        Map<String, LinkedHashSet<Packet>> neoPackets = new LinkedHashMap<>();
        for(Map.Entry<String,File> entry: protocolsFileMap.entrySet()) {
            try {
                JsonMapper versionMappedProtocol = mapper.readValue(entry.getValue(), JsonMapper.class);
             //   versionMappedProtocol;
                versionProtocols.put(entry.getKey(), versionMappedProtocol);

                for(Map.Entry<String, AbstractBehavioural> typeEntry: versionMappedProtocol.getTypes().entrySet()){
                    String typeName = typeEntry.getKey().toLowerCase();
                    AbstractBehavioural type = typeEntry.getValue();

                    if(!globalTypes.containsKey(typeName)) {
                        Map<String, AbstractBehavioural> nMap = new LinkedHashMap<>();
                        nMap.put(entry.getKey(), type);
                        globalTypes.put(typeName, nMap);
                    } else {
                        globalTypes.get(typeName).put(entry.getKey(), type);
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

        //builds map version -> (typeName -> finalName)
        final Map<String, Map<String, String>> versionedFinalNameMap = new LinkedHashMap<>();

        for(Map.Entry<String, Map<String, AbstractBehavioural>> entry : globalTypes.entrySet()){
            Map<String, AbstractBehavioural> typeVariations = entry.getValue();
            for(Map.Entry<String, AbstractBehavioural> typeVariation : typeVariations.entrySet()){
              //  Map<String, String> val = new LinkedHashMap<>();
                Collection<AbstractBehavioural> variations = typeVariations.values();
                Set<AbstractBehavioural> uniqueVariations = new LinkedHashSet<>(variations);
                String finalName = entry.getKey();
                if(uniqueVariations.size() > 1) {
                    finalName += typeVariation.getValue().asPacketFields().stream()
                            .map(PacketField::getName)
                            .collect(Collectors.joining(""));
                }
                versionedFinalNameMap.computeIfAbsent(entry.getKey(), k -> new LinkedHashMap<>()).put(typeVariation.getKey(), finalName);

            }
        }


        List<Map.Entry<String, AbstractBehavioural>> unbuildableTypes = new ArrayList<>();
   /*     for(Map.Entry<String, Set<AbstractBehavioural>> entry : globalTypes.entrySet()) {
            for(AbstractBehavioural type : entry.getValue()) {
                if(!type.isBuildable()){
                    unbuildableTypes.add(new AbstractMap.SimpleEntry<>(entry.getKey(), type) {});
                }
            }
        }
*/
        //create multiversion packets from map<packetName, Set<Packet>>
        Map<String, MultiVersionPacket> mvps = new LinkedHashMap<>();
        for(Map.Entry<String, LinkedHashSet<Packet>> packetType : neoPackets.entrySet()) {
            String packetTypeName = packetType.getKey().toLowerCase();

            Packet firstInstance = packetType.getValue().getFirst();
            LinkedHashSet<PacketField> initFields = new LinkedHashSet<>(firstInstance.asPacketFields());
            MultiVersionPacket mvp = new MultiVersionPacket(packetTypeName, firstInstance.getVersion(), initFields);

            for(Packet packet : packetType.getValue()) {
                LinkedHashSet<PacketField> fields = new LinkedHashSet<>(packet.asPacketFields());
                mvp.addVersion(packet.getVersion(), fields);
            }
            mvps.put(packetTypeName, mvp);
        }

      //  MultiVersionPacket.setVersionedFinalNames(versionedFinalNameMap);
        ClassDescriptor.setResolveMap(versionedFinalNameMap);

        //write content
        StringBuilder fileContent = new StringBuilder("import java.util.*;\nimport java.nio.ByteBuffer;\n" +
                "import java.math.BigInteger;\n\n");
        Set<String> generatedTypes = new HashSet<>();
        Map<String, String> typeNameToClassName = new LinkedHashMap<>();

        // Generate type classes with names as referenced in packets FIRST
        StringBuilder typeContent = new StringBuilder();
        for (Map.Entry<String, String> entry : typeNameToClassName.entrySet()) {
            String refName = entry.getKey();
            String className = entry.getValue();
            // Find the AbstractBehavioural for this type
            AbstractBehavioural type = null;
            for (Map<String, AbstractBehavioural> vmap : globalTypes.values()) {
                for (Map.Entry<String, AbstractBehavioural> e : vmap.entrySet()) {
                    if (e.getValue().getClassDescriptor() != null && className.equals(e.getValue().getClassDescriptor().getName())) {
                        type = e.getValue();
                        break;
                    }
                }
                if (type != null) break;
            }
            if (type != null && !generatedTypes.contains(className)) {
                StringBuilder typeClass = new StringBuilder("class " + className + " extends TypeBase{\n");
                typeClass.append(type.generateFieldDeclarations());
                typeClass.append(type.generateConstructors());
                typeClass.append(type.generateGetters());
                typeClass.append("}\n");
                typeContent.append(typeClass).append("\n");
                generatedTypes.add(className);
            }
        }
        // Now generate packet classes
        for(MultiVersionPacket mvp : mvps.values()) {
            if(mvp.getName().equals("packet")){
                continue;
            }
            StringBuilder packetClass = new StringBuilder("class " + mvp.getName() + " extends PacketBase{\n");
            packetClass.append(mvp.generateFieldDeclarations());
            packetClass.append(mvp.generateConstructors());
          //  packetClass.append(mvp.generateRead());
            packetClass.append(mvp.generateGetters());
            packetClass.append("}\n");
            fileContent.append(packetClass).append("\n");
        }
        // Write types first, then packets
        writeToFile("out.java", indentString(fileContent.insert(0, typeContent).toString()));
    }
    public static void writeToFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);

        // Make sure the directory hierarchy exists
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        // Create or overwrite the file with UTF‑8 text
        Files.writeString(
                path,
                content,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    public static String indentString(String input) {
        StringBuilder result = new StringBuilder();
        int indentLevel = 0;
        boolean increaseIndent = false;

        for (String line : input.split("\n", -1)) { // Preserve empty lines
            String trimmedLine = line.stripLeading();

            // Adjust indentation before appending
            if (trimmedLine.startsWith("}") || trimmedLine.startsWith("){")|| trimmedLine.startsWith(");")) {
                indentLevel = Math.max(0, indentLevel - 1);
            }

            result.append("\t".repeat(indentLevel)).append(trimmedLine).append("\n");

            // Increase indent for class and block openings
            increaseIndent = trimmedLine.matches("(class|static class|public class)\\s+\\w+.*\\{\\s+")
                    || trimmedLine.endsWith("{")
                    || trimmedLine.endsWith("(")
                    && !trimmedLine.equals("){");
            if (increaseIndent) {
                indentLevel++;
            }
        }

        return result.toString();
    }


    public static String sizeString(MultiVersionPacket mvp){
        return null;
    }



}