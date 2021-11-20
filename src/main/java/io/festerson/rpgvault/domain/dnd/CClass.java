package io.festerson.rpgvault.domain.dnd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection="dnd5e-classes")
public class CClass {
    private String index;
    private String name;
    private Integer hit_die;
    private List<OptionsTrinity> proficiency_choices;
    private List<ValueTrinity> proficiencies;
    private List<ValueTrinity> saving_throws;
    private List<Equipment> starting_equipment;
    private List<EquipmentOptions> starting_equipment_options;
    private String class_levels;
    private Multiclassing multi_classing;
    private Spellcasting spellcasting;
    private String spells;
    private List<ValueTrinity> subclasses;
    private String url;

    @SuppressWarnings("unchecked")
    @JsonProperty("starting_equipment_options")
    private void unpackNested(Map<String,Object> starting_equipment_options) {
        Object from = starting_equipment_options.get("from");
    }
}
