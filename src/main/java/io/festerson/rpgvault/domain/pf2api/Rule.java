package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Rule {
    private List<String> add;
    private String key;
    private String type;
    private String label;
    private String selector;
    private String mode;
    private String path;
    private Object value;
}
