package io.festoso.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpellComponents {
    private Boolean focus;
    private Boolean material;
    private Boolean somatic;
    private Boolean verbal;
}
