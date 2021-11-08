package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AncestryResponse {
    private Integer count;
    private List<AncestryResult> results;
}
