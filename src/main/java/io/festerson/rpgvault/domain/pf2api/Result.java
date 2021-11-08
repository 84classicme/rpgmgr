package io.festerson.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public abstract class Result {
    private String id;
    private List<Object> effects;
    private String name;
    private String type;
}
