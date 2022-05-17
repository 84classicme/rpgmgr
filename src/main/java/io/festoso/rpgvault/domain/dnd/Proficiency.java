package io.festoso.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection="dnd5e-proficiencies")
public class Proficiency {
    private String index;
    private String type;
    private String name;
    private List<String> classes;
    private List<String> races;
    private ValueTrinity reference;
    private List<ValueTrinity> references;
    private String url;
}
