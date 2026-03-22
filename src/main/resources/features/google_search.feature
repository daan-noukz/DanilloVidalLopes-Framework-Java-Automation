Feature: Google Search

  @regressao
Scenario: Perform a simple search
  Given I open the Google page
  When I type "hello world" in the search bar
  Then I should see the results page for "hello world"
