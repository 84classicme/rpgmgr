package io.festerson.rpgvault.domain.pf2api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Rule {
    private List<String> add;
    private String key;
    private String type;
    private String label;
    private String selector;
    private String mode;
    private List<String> outcomes;
    private String path;
    private Predicate predicate;
    private String text;
    private List<String> traits;
    private Object value;

    @JsonProperty("outcome")
    private void unpackNestedItems(Object o) {
        outcomes = new ArrayList();
        if(o instanceof String){
            outcomes.add((String)o);
        } else if (o instanceof ArrayList) {
            outcomes = (ArrayList<String>)o;
        }
    }
}
