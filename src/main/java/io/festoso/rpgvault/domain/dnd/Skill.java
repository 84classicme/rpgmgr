package io.festoso.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection="dnd5e-skills")
public class Skill {
    private String index;
    private String name;
    private List<String> desc;
    private ValueTrinity ability_score;
    private String url;
}
