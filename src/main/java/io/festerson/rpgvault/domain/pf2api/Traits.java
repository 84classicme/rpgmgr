package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Traits {
    private String custom;
    private List<String> value;
    private ValueString rarity;
}
