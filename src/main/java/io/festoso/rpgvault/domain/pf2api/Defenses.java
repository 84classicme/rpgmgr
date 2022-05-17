package io.festoso.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Defenses {
    private Integer heavy;
    private Integer light;
    private Integer medium;
    private Integer unarmored;
}
