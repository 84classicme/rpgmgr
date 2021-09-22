#Feature: Verify springboot application using Cucumber
#
#  @ReceiveUserDetails
#  Scenario Outline: Send a valid Request to get user details
#    Given I send a request to the URL "/students" to get user details
#    Then the response will return status 200 and id <studentID> and names "<studentNames>" and passport_no "<studentPassportNo>"
#
#    Examples:
#      |studentID    |studentNames  |studentPassportNo|
#      |10001        |Annie         |E1234567         |
#      |10002        |John          |A1234568         |
#      |10003        |David         |C1232268         |