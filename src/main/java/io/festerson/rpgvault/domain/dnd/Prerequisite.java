package io.festerson.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Prerequisite {
    private Integer minimum_score;
    private ValueTrinity ability_score;
}
