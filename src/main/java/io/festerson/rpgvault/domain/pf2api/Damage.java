package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Damage {
    private String damageType;
    private Integer dice;
    private String die;
    private String value;
}
