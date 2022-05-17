package io.festoso.rpgvault.domain.pf2api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class  Traits {
    private String custom;
    private List<String> value;
    private ValueString rarity;
    @JsonIgnore
    private List<String> selected;
}
