package io.festerson.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OptionsTrinity {
    private Integer choose;
    private String type;
    private List<ValueTrinity> from;
}
