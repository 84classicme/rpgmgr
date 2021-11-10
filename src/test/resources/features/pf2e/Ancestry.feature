@RPGmgr @PF2e @Ancestry

Feature: Fetch ancestry data from remote PF2e SRD rest endpoints

  @GetAllAncestries
  Scenario:  Get details for all ancestries
    When I request information about all ancestries by using a GET url
    Then the response will return http status ok and data for all ancestries