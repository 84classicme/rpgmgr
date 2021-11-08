@PF2e @Equipment

Feature: Fetch equipment data from remote PF2e SRD rest endpoints

  @GetAllEquipment
  Scenario:  Get details for all equipment
    When I request information about all equipment by using a GET url
    Then the response will return http status ok and data for all equipment