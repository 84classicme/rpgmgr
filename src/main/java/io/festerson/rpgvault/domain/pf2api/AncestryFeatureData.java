package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AncestryFeatureData {
    private ValueString actionCategory;
    private ValueString actionType;
    private ValueInteger actions;
    private ValueString description;
    private ValueString featType;
    private ValueString level;
    private String location;
    private ValueObjectList prerequisites;
    private List<Rule> rules;
    private ValueString source;
    private Traits traits;
}
