@RPGmgr @Players

Feature: Manage player data via CRUD interface

  @GetPlayer
  Scenario Outline:  Get details for given player id
    Given player information is available in the database
    When I include the <playerid> in the url of the request
    Then the response will return http status ok and player first name <firstname> and last name <lastname> and email <email> and image url <imageurl>

    Examples:
      |playerid     |firstname      |lastname    |email                 | imageurl                  |
      |10001        |Annie          |Jones       |aj@example.com        | http://example.com/aj.jpg |
      |10002        |John           |Johnson     |jj@example.com        | http://example.com/jj.jpg |
      |10003        |David          |Davidson    |dd@example.com        | http://example.com/dd.jpg |

  @GetAllPlayers
  Scenario:  Get details for all players
    Given player information is available in the database
    When I request information about all players by using a GET url
    Then the response will return http status ok and data for all players
      |firstName      |lastName    |email          |country   | imageUrl                  |
      |Annie          |Jones       |aj@example.com | US       | http://example.com/aj.jpg |
      |John           |Johnson     |jj@example.com | IT       | http://example.com/jj.jpg |
      |David          |Davidson    |dd@example.com | IN       | http://example.com/dd.jpg |

  @CreatePlayer
  Scenario Outline:  Create new players
    When I create a new player
    * first name is <firstname>
    * last name is <lastname>
    * country is <country>
    * email is <email>
    * image url is <imageurl>
    And I POST a request into the system
    Then the system creates the record and responds with http status code 201 and returns the new player with a unique id

    Examples:
      |firstname      |lastname    |country |email                 | imageurl                  |
      |Donnie         |Phillips    |US      |dp@example.com        | http://example.com/dp.jpg |
      |Earl           |Monroe      |India   |em@example.com        | http://example.com/em.jpg |
      |Franklin       |Freewheel   |Italy   |ff@example.com        | http://example.com/ff.jpg |

  @UpdatePlayer
  Scenario Outline:  Update details for given player by id
    Given player data exists that needs to be updated
    When I have new <firstname> <lastname> <email> <country> or <imageurl> data for a <playerid>
    And I PUT a request into the system
    Then the system updates the record and responds with http status code 200 and returns the updated player

    Examples:
      |playerid     |email                      |firstname       |lastname       |country | imageurl                     |
      | 10010        | ps@NEWexample.com        |Pat             |Samples        |US      | http://example.com/ps.jpg    |
      | 10011        | bh@example.com           |NEW             |Hur            |US      | http://example.com/bh.jpg    |
      | 10012        | pm@example.com           |Peaches         |NEW            |US      | http://example.com/pm.jpg    |
      | 10013        | ls@example.com           |Lisa            |Simpson        |NEW     | http://example.com/ls.jpg    |
      | 10014        | aw@example.com           |Adam            |West           |US      | http://NEWexample.com/aw.jpg |

  @DeletePlayer
  Scenario Outline:  Delete a given player by id
    Given player data exists that need to be deleted
    When I include the <playerid> in the url of the request
    Then the system deletes the record and responds with http status no content
    And I can no longer get the player data by using the <playerid>

    Examples:
      |playerid|
      |10020   |
      |10021   |
      |10022   |

  @ValidatePlayerOnCreate
  Scenario Outline:  Validate player details
    When an invalid <firstname> <lastname> or <email> is used in player data
    And I POST a request into the system
    Then the system responds with http status code 400 and a <message> about each invalid field in the POST

    Examples:
      |firstname |lastname |email           | message                                                                                              |
      |empty     |Jones    |aj@example.com  | 'firstName must not be blank.'                                                                       |
      |John      |empty    |jj@example.com  | 'lastName must not be blank.'                                                                        |
      |David     |Davidson |example.com     | 'email must be a well-formed email address.'                                                         |
      |empty     |empty    |example.com     | 'firstName must not be blank.lastName must not be blank.email must be a well-formed email address.'  |
      |null      |Jones    |aj@example.com  | 'firstName must not be blank.'                                                                       |
      |John      |null     |jj@example.com  | 'lastName must not be blank.'                                                                        |
      |null      |null     |example.com     | 'firstName must not be blank.lastName must not be blank.email must be a well-formed email address.'  |

  @ValidatePlayerOnUpdate
  Scenario Outline:  Validate player details
    When an invalid <firstname> <lastname> or <email> is used in player data
    And I PUT a request into the system
    Then the system responds with http status code 400 and a <message> about each invalid field in the PUT

    Examples:
      |firstname |lastname |email           | message                                                                                              |
      |empty     |Jones    |aj@example.com  | 'firstName must not be blank.'                                                                       |
      |John      |empty    |jj@example.com  | 'lastName must not be blank.'                                                                        |
      |David     |Davidson |example.com     | 'email must be a well-formed email address.'                                                         |
      |empty     |empty    |example.com     | 'firstName must not be blank.lastName must not be blank.email must be a well-formed email address.'  |
      |null      |Jones    |aj@example.com  | 'firstName must not be blank.'                                                                       |
      |John      |null     |jj@example.com  | 'lastName must not be blank.'                                                                        |
      |null      |null     |example.com     | 'firstName must not be blank.lastName must not be blank.email must be a well-formed email address.'  |