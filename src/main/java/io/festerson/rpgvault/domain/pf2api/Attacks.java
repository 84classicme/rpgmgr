package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Attacks {
    private Integer advanced;
    private Integer martial;
    private Integer simple;
    private Integer unarmed;
    private AttacksOther other;
}
