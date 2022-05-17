package io.festoso.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection="dnd5e-languages")
public class Language {
    private String index;
    private String name;
    private String desc;
    private String type;
    private List<String> typical_speakers;
    private String script;
    private String url;
}
