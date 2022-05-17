package io.festoso.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FeatData {
    private ValueString actionCategory;
    private ValueString actionType;
    private ValueString actions;
    private ValueString description;
    private ValueString featType;
    private ValueInteger level;
    private ValueObjectList prerequisites;
    private List<Rule> rules;
    private ValueString source;
    private Traits traits;
}
