package io.festoso.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SpellData {
    private ValueString ability;
    private SpellArea area;
    private ValueString areasize;
    private ValueInteger autoHeightenLevel;
    private ValueString category;
    private SpellComponents components;
    private ValueString cost;
    private ValueString description;
    private ValueString duration;
    private ValueBoolean hasCounteractCheck;
    private ValueInteger level;
    private ValueString location;
    private ValueString materials;
    private ValueString prepared;
    private ValueString primarycheck;
    private ValueString range;
    private List<Rule> rules;
    private SpellSave save;
    private ValueString school;
    private ValueString secondarycasters;
    private ValueString secondarycheck;
    private ValueString source;
    private ValueString spellType;
    private ValueBoolean sustained;
    private ValueString target;
    private ValueString time;
    private SpellTraditions traditions;
    private Traits traits;
}
