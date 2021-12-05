package io.festerson.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection="dnd5e-subraces")
public class Subrace {
    private String index;
    private String name;
    private String desc;
    private List<AbilityBonus> ability_bonuses;
    private List<ValueTrinity> starting_proficiencies;
    private List<ValueTrinity> languages;
    OptionsTrinity language_options;
    private List<ValueTrinity> racial_traits;
    private String url;
}
