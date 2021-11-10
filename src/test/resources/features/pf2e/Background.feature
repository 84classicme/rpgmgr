@RPGmgr @PF2e @Background

Feature: Fetch background data from remote PF2e SRD rest endpoints

  @GetAllBackgrounds
  Scenario:  Get details for all backgrounds
    When I request information about all backgrounds by using a GET url
    Then the response will return http status ok and data for all backgrounds