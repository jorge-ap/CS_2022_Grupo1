Feature: Buy improvement

Scenario: Buy first improvement
  Given I have clicked 10 times the coin button
  When I press the "Tienda" button
    And I press the "Comprar mejora" button
    And I press the "Atrás" button
    And I click 1 times the coin button
  Then I see the text "2"
  And I take a screenshot
