package io.festoso.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ClassFeatureResponse {
    private Integer count;
    private List<ClassFeatureResult> results;
}
