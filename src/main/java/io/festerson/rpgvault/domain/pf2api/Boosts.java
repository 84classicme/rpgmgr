package io.festerson.rpgvault.domain.pf2api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Boosts {
    @JsonProperty("0")
    private ValueStringList boost0;
    @JsonProperty("1")
    private ValueStringList boost1;
    @JsonProperty("2")
    private ValueStringList boost3;
}
