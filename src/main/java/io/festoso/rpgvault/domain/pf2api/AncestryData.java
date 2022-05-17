package io.festoso.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AncestryData extends ItemData{
    private AdditionalLanguages additionalLanguages;
    private Boosts boosts;
    private ValueString description;
    private Flaws flaws;
    private Integer hp;
    private Languages languages;
    private Integer reach;
    private List<Rule> rules;
    private String size;
    private ValueString source;
    private Integer speed;
    private Traits traits;
    private String vision;
}
