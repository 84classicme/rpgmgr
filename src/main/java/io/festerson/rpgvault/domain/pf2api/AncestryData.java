package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AncestryData {
    private AdditionalLanguages additionalLanguages;
    private Boosts boosts;
    private ValueString description;
    private Flaws flaws;
    private Integer hp;
    private Languages languages;
    private Integer reach;
    private String size;
    private ValueString source;
    private Integer speed;
    private Traits traits;
    private String vision;
}
