package io.festoso.rpgvault.domain.pf2api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemData {
    private List<Item> itemList;

    @SuppressWarnings("unchecked")
    @JsonProperty("items")
    private void unpackNestedItems(Map<String,Object> items) {
        itemList = new ArrayList();
        items.forEach((s, o) -> {
            Item i = new Item();
            i.setId((String)((LinkedHashMap)o).get("id"));
            i.setImg((String)((LinkedHashMap)o).get("img"));
            i.setName((String)((LinkedHashMap)o).get("name"));
            i.setLevel(convertToInteger(((LinkedHashMap)o).get("level")));
            i.setPack((String)((LinkedHashMap)o).get("pack"));
            itemList.add(i);
        });
    }

    private Integer convertToInteger(Object o){
        if(o instanceof String){
            return Integer.parseInt((String)o);
        } else if (o instanceof Integer) {
            return (Integer)o;
        }
        return null;
    }
}
