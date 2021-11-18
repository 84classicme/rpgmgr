package io.festerson.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Equipment {
    private int quantity;
    private ValueTrinity equipment;
}
