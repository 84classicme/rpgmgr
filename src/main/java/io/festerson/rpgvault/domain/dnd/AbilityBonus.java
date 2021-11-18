package io.festerson.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AbilityBonus {
    private ValueTrinity ability_score;
    private Integer bonus;
}
