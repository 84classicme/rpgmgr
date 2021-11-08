@PF2e @Spell

Feature: Fetch spell data from remote PF2e SRD rest endpoints

  @GetAllSpells
  Scenario:  Get details for all spells
    When I request information about all spells by using a GET url
    Then the response will return http status ok and data for all spells