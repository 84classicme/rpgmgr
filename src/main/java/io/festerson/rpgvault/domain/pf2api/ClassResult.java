package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection="pf2e-classes")
public class ClassResult extends Result{
    private ClassData data;
}
