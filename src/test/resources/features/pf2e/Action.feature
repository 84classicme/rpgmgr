@PF2e @Action

Feature: Fetch action data from remote PF2e SRD rest endpoints

  @GetAllActions
  Scenario:  Get details for all actions
    When I request information about all actions by using a GET url
    Then the response will return http status ok and data for all actions