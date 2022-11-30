package io.festoso.rpgvault.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection="campaigns")
public class Chronicle {
    private LocalDateTime date;
    private String gm;
    private String scenario;
    private Integer points;
    private String event;
    private Integer session;
    private String player;
    private String character;
    private String faction;
    private Integer reputation;
    private String notes;
}
