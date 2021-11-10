@RPGmgr @PF2e @Feat

Feature: Fetch feat data from remote PF2e SRD rest endpoints

  @GetAllFeats
  Scenario:  Get details for all feats
    When I request information about all feats by using a GET url
    Then the response will return http status ok and data for all feats