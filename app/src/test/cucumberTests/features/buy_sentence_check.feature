Feature:
  As a user
  I want to know how many coins do I need to buy a sentence
  So that I can save to buy it

  Scenario: I want to be told how much I need to buy a phrase
    Scenario Outline:
      Given I have <start> coins
      When I need <coins_needed> coins
      Then I should have <more> more coins

      Examples:
        | start | coins_needed | more |
        | 10    | 50           | 40   |
        | 40    | 50           | 10   |