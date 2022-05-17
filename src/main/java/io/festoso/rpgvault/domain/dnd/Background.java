package io.festoso.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection="dnd5e-backgrounds")
public class Background {
    private String index;
    private String name;
    private List<ValueTrinity> starting_proficiencies;
    private OptionsTrinity language_options ;
    private List<Equipment> starting_equipment;
    private ValueDuo feature;
    private OptionsString personality_traits;
    private OptionsString ideals;
    private OptionsString bonds;
    private OptionsString flaws;
    private String url;
}
