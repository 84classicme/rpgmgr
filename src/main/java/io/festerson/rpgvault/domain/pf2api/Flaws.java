package io.festerson.rpgvault.domain.pf2api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Flaws {
    @JsonProperty("0")
    private ValueStringList flaw0;
}
