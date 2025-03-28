import Behaviourals.AbstractBehavioural;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class JsonMapper {

    private boolean isBehavioural(String s){
        try {
            BehaviouralFactory.valueOf(s.toUpperCase());
            return true;
        } catch (Exception e){
            return false;
        }
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
                }
                BehaviouralFactory.addKnownBehavioural(entry.getKey(), bh);
            }
        }
    }
    @JsonAnySetter
    public void SetPacket(String Key, Object value){
        System.out.println(Key + ": " + value);
    }

}
