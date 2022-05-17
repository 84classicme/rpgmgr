package io.festoso.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BackgroundData extends ItemData {
    private Boosts boosts;
    private ValueString description;
    private List<Rule> rules;
    private ValueString source;
    private String trainedLore;
    private TrainedSkills trainedSkills;
    private Traits traits;
}
