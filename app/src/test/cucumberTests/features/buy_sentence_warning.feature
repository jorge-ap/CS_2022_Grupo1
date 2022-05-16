Feature:
  As a user
  I want to get notice when I can buy a sentence
  So that I can buy it when I got the needed coins

Scenario: I want to be notified when I can buy a sentence
  Scenario Outline:
    Given I have <start> coins
    When I have <my_coins> coins
    Then I should see <coins_showed> coins

    Examples:
      | start | my_coins | coins_showed      |
      | 0     | 10       | 10                |
      | 0     | 50       | "CREA TU FRASE"   |