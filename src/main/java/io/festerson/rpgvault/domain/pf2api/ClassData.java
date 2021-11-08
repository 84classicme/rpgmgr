package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ClassData {
    private ValueIntegerList abilityBoostLevels;
    private ValueIntegerList ancestryFeatLevels;
    private Attacks attacks;
    private Integer classDC;
    private ValueIntegerList classFeatLevels;
    private Defenses defenses;
    private ValueString description;
    private ValueIntegerList generalFeatLevels;
    private Integer hp;
    private ValueStringList keyAbility;
    private Integer perception;
    private List<Rule> rules;
    private SavingThrows savingThrows;
    private ValueIntegerList skillFeatLevels;
    private ValueIntegerList skillIncreaseLevels;
    private ValueString source;
    private TrainedSkills trainedSkills;
    private Traits traits;
}
