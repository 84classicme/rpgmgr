package io.festoso.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection="dnd5e-alignments")
public class Alignment {
    private String index;
    private String name;
    private String abbreviation;
    private String desc;
    private String url;
}
