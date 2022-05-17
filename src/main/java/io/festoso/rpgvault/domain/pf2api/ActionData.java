package io.festoso.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ActionData {
    private ValueString actionCategory;
    private ValueString actionType;
    private ValueInteger actions;
    private ValueString description;
    private ValueString requirements;
    private List<Object> rules;
    private ValueString source;
    private Traits traits;
    private ValueString trigger;
    private ValueString weapon;
}
