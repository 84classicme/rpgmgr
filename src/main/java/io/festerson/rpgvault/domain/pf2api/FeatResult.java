package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection="pf2e-feats")
public class FeatResult extends Result{
    private FeatData data;
}
