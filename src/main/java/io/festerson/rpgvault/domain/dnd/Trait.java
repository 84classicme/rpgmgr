package io.festerson.rpgvault.domain.dnd;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection="dnd5e-traits")
public class Trait {
    private String index;
    private String name;
    private List<String> desc;
}
