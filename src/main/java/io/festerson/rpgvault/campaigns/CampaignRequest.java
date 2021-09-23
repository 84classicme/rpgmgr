package io.festerson.rpgvault.campaigns;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignRequest {

    private List<String> monsters;
    private List<String> npcs;
}
