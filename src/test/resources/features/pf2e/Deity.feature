@RPGmgr @PF2e @Deity

Feature: Fetch deity data from remote PF2e SRD rest endpoints

  @GetAllDeities
  Scenario:  Get details for all class features
    When I request information about all deities by using a GET url
    Then the response will return http status ok and data for all deities