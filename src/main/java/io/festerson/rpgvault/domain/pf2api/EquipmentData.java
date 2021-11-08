package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EquipmentData {
    private String baseItem;
    private ValueString brokenThreshold;
    private ValueString bulkCapacity;
    private ValueBoolean collapsed;
    private ValueString containerId;
    private ValueString description;
    private ValueBoolean equipped;
    private ValueString equippedBulk;
    private ValueInteger hardness;
    private ValueInteger hp;
    private Identification identification;
    private ValueBoolean invested;
    private ValueInteger level;
    private ValueInteger maxHp;
    private ValueString negateBulk;
    private ValueString preciousMaterial;
    private ValueString preciousMaterialGrade;
    private ValueString price;
    private ValueInteger quantity;
    private ValueString size;
    private ValueString source;
    private ValueString stackGroup;
    private Traits traits;
    private ValueString usage;
    private ValueString weight;
}
