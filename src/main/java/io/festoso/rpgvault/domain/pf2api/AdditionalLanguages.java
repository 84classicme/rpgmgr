package io.festoso.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AdditionalLanguages {
    private Integer count;
    private String custom;
    private List<String> value;
}
