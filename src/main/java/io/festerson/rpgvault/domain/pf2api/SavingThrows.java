package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SavingThrows {
    private Integer fortitude;
    private Integer reflex;
    private Integer will;
}
