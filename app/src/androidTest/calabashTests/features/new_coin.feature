Feature: Get new coins

Scenario: Get first by pressing button
  Given I see the text "0"
  When I press "bt_moneda"
  Then I see the text "1"

Scenario: Get one more coin by pressing button
  Given I have clicked 1 times the coin button
  When I press "bt_moneda"
  Then I see the text "2"
