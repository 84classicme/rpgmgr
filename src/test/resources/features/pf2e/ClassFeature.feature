@RPGmgr @PF2e @ClassFeature

Feature: Fetch class feature data from remote PF2e SRD rest endpoints

  @GetAllClassFeatures
  Scenario:  Get details for all class features
    When I request information about all class features by using a GET url
    Then the response will return http status ok and data for all class features