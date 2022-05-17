package io.festoso.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection="dnd5e-levels")
public class Level {
    private Integer level;
    private ValueTrinity cclass;
    private Integer ability_score_bonuses;
    private Integer prof_bonus;
    private List<ValueTrinity> features;
    private LevelSpellcasting spellcasting;
}
