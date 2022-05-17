package io.festoso.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Spellcasting {
    private Integer level;
    private ValueTrinity spellcasting_ability;
    private List<ValueDuo> info;

}
