import Behaviourals.BehaviouralType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class JsonMapper {

    @JsonProperty("types")
    public void SetTypes(Map<String, Object> types){
        for(Map.Entry<String, Object> entry: types.entrySet()){
            BehaviouralType bh;
            if(entry.getValue() instanceof String s && s.equals("native")) {
            bh = BehaviouralFactory.createBehavioural(entry.getKey());
            } else {
                bh = BehaviouralFactory.createBehavioural(entry.getValue());
            }
            BehaviouralFactory.addKnownBehavioural(entry.getKey(), bh);
        }
    }
    @JsonAnySetter
    public void SetPacket(String Key, Object value){
        System.out.println(Key + ": " + value);
    }

}
