@PF2e @Class

Feature: Fetch classes data from remote PF2e SRD rest endpoints

  @GetAllClasses
  Scenario:  Get details for all classes
    When I request information about all classes by using a GET url
    Then the response will return http status ok and data for all classes