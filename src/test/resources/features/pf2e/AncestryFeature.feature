@PF2e @AncestryFeature

Feature: Fetch ancestry feature data from remote PF2e SRD rest endpoints

  @GetAllAncestryFeatures
  Scenario:  Get details for all ancestry features
    When I request information about all ancestry features by using a GET url
    Then the response will return http status ok and data for all ancestry features