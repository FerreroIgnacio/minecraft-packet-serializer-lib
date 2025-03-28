import Behaviourals.AbstractBehavioural;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class JsonMapper {

    @JsonProperty("types")
    public void SetTypes(Map<String, Object> types){
        for(Map.Entry<String, Object> entry: types.entrySet()) {
            AbstractBehavioural bh = null;
            if (entry.getValue() instanceof String s && s.equals("native")) {
                try {
                    BehaviouralFactory.valueOf(entry.getKey().toUpperCase());
                } catch (IllegalArgumentException e) {
                    bh = BehaviouralFactory.createBehavioural(entry.getKey());
                }
            } else {
                bh = BehaviouralFactory.createBehavioural(entry.getValue());
            }
            if (bh != null) {
                BehaviouralFactory.addKnownBehavioural(entry.getKey(), bh);
            }
        }
    }
    @JsonAnySetter
    public void SetPacket(String Key, Object value){
        System.out.println(Key + ": " + value);
    }

}
