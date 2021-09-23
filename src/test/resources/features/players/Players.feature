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
      |id           |firstName      |lastName    |email                 | imageUrl                  |
      |10001        |Annie          |Jones       |aj@example.com        | http://example.com/aj.jpg |
      |10002        |John           |Johnson     |jj@example.com        | http://example.com/jj.jpg |
      |10003        |David          |Davidson    |dd@example.com        | http://example.com/dd.jpg |

  @CreatePlayer
  Scenario Outline:  Create new players
    When I request to create a new player by POSTING their first name <firstname> and last name <lastname> and email <email> and image url <imageurl>
    Then the system creates the record and responds with http status created and player first name <firstname> and last name <lastname> and email <email> and image url <imageurl>

    Examples:
      |firstname      |lastname     |email                 | imageurl                  |
      |Donnie         |Phillips    |aj@example.com        | http://example.com/dp.jpg |
      |Earl           |Monroe      |jj@example.com        | http://example.com/em.jpg |
      |Franklin       |Freewheel   |dd@example.com        | http://example.com/ff.jpg |

  @UpdatePlayer
  Scenario Outline:  Update details for given player by id
    Given player data exists that needs to be updated
    When I update the email of player <firstname> <lastname> to <newemail> by using their <playerid> in a PUT url
    Then the system updates the record and responds with http status ok and player id <playerid> and first name <firstname> and last name <lastname> and email <newemail> and image url <imageurl>

    Examples:
      |playerid     |newemail                 |firstname       |lastname       | imageurl                  |
      |10004        |ps@newexample.com        |Pat             |Samples        | http://example.com/ps.jpg |
      |10005        |bh@newexample.com        |Ben             |Hur            | http://example.com/bh.jpg |
      |10006        |pm@newexample.com        |Peaches         |Moscowitz      | http://example.com/pm.jpg |

  @DeletePlayer
  Scenario Outline:  Delete a given player by id
    Given player data exists that need to be deleted
    When I include the <playerid> in the url of the request
    Then the system deletes the record and responds with http status no content
    And I can no longer get the player data by using the <playerid>

    Examples:
      |playerid|
      |10007   |
      |10008   |
      |10009   |

  @ValidatePlayer
  Scenario Outline:  Validate player details
    When an invalid <firstname> <lastname> or <email> is used in player data
    Then the response returns http status bad request and include the message <message> about the validation errors when creating a player
    And the response returns http status bad request and include the message <message> about the validation errors when updating a player

    Examples:
      |firstname |lastname |email           | message                                                    |
      |empty     |Jones    |aj@example.com  | 'firstName must not be blank.'                             |
      |John      |empty    |jj@example.com  | 'lastName must not be blank.'                              |
      |David     |Davidson |example.com     | 'email must be a well-formed email address.'               |
      |empty     |empty    |example.com     | 'firstName must not be blank.lastName must not be blank.email must be a well-formed email address.'  |
      |null      |Jones    |aj@example.com  | 'firstName must not be blank.'                             |
      |John      |null     |jj@example.com  | 'lastName must not be blank.'                              |
      |null      |null     |example.com     | 'firstName must not be blank.lastName must not be blank.email must be a well-formed email address.'  |