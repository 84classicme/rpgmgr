package io.festoso.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.util.List;

@Data
@NoArgsConstructor
public abstract class Result {
    @Id
    private String _id;
    private List<Object> effects;
    private String name;
    private String type;
}
