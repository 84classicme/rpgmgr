package io.festoso.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EquipmentProperty {
    private String critDamage;
    private String critDamageType;
    private Integer critDice;
    private String critDie;
    private String criticalConditionType;
    private Integer criticalConditionValue;
    private String damageType;
    private Integer dice;
    private String die;
    private String strikeConditionType;
    private Integer strikeConditionValue;
    private String value;
}
