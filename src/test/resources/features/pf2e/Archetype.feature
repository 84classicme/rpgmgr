@PF2e @Archetype

Feature: Fetch archetype data from remote PF2e SRD rest endpoints

  @GetAllArchetypes
  Scenario:  Get details for all archetypes
    When I request information about all archetypes by using a GET url
    Then the response will return http status ok and data for all archetypes