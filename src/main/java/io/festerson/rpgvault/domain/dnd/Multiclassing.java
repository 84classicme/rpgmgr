package io.festerson.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Multiclassing {
    private List<Prerequisite> prerequisites;
    private List<ValueTrinity> proficiencies;
}
