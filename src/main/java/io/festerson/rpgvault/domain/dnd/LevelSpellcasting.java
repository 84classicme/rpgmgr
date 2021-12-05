package io.festerson.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LevelSpellcasting {
    private Integer cantrips_known;
    private Integer spells_known;
    private Integer spell_slots_level_1;
    private Integer spell_slots_level_2;
    private Integer spell_slots_level_3;
    private Integer spell_slots_level_4;
    private Integer spell_slots_level_5;
    private Integer spell_slots_level_6;
    private Integer spell_slots_level_7;
    private Integer spell_slots_level_8;
    private Integer spell_slots_level_9;
}
