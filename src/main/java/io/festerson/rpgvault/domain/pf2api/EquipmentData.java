package io.festerson.rpgvault.domain.pf2api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EquipmentData {
    private String baseItem;
    private ValueString ability;
    private ValueInteger bonus;
    private ValueInteger bonusDamage;
    private ValueString brokenThreshold;
    private ValueString bulkCapacity;
    private ValueBoolean collapsed;
    private ValueString containerId;
    private Damage damage;
    private ValueString description;
    private ValueBoolean equipped;
    private ValueString equippedBulk;
    private ValueString group;
    private ValueString hands;
    private ValueInteger hardness;
    private ValueInteger hp;
    private Identification identification;
    private ValueBoolean invested;
    private ValueInteger level;
    @JsonProperty("MAP")
    private ValueString map;
    private ValueInteger maxHp;
    private ValueString negateBulk;
    private ValueBoolean potencyRune;
    private ValueString preciousMaterial;
    private ValueString preciousMaterialGrade;
    private ValueString price;
    private EquipmentProperty property1;
    private ValueString propertyRune1;
    private ValueString propertyRune2;
    private ValueString propertyRune3;
    private ValueString propertyRune4;
    private ValueInteger quantity;
    private ValueString range;
    private ValueString reload;
    private List<Rule> rules;
    private ValueString size;
    private ValueString source;
    private ValueInteger splashDamage;
    private ValueString stackGroup;
    private ValueString strikingRune;
    private Traits traits;
    private ValueString usage;
    private ValueString weaponType;
    private ValueString weight;
}
