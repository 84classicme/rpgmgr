package io.festoso.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection="dnd5e-races")
public class Race {
    private String index;
    private String name;
    private Integer speed;
    private List<AbilityBonus> ability_bonuses;
    private AbilityOptionsTrinity ability_bonus_options;
    private String alignment;
    private String age;
    private String size;
    private String size_description;
    private List<ValueTrinity> starting_proficiencies;
    OptionsTrinity starting_proficiency_options;
    private List<ValueTrinity> languages;
    private String language_desc;
    private List<ValueTrinity> traits;
    private List<ValueTrinity> subraces;
    private String url;
}
