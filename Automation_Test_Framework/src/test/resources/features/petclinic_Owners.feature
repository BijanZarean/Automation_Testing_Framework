@ui @smoke @owners @regression
  Feature: Owners page

@owner1
Scenario: Navigate to the owners page
  Given I open the Petclinic application
  When I open the Owners page
  Then the Owners page should be displayed

@owner2
Scenario: Display all existing owners
  Given I am on the Owners page
  Then at least one owner should be listed
  And every owner row should contain a name and address

@owner3
Scenario: Search for an existing owner by last name
  Given I am on the New Owner page
  When I create an owner
  And I search for the owner
  Then every displayed owner should have the correct last name