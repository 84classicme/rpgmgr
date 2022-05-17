package io.festoso.rpgvault.domain.pf2api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ValueObjectList {
    private List<Object> value;
}
