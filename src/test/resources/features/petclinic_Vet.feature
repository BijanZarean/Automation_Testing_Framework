@ui @smoke @vet
Feature: Petclinic home page

  @ui @smoke @vet1
  Scenario: Open the Veterinarians page
  Given I open the Petclinic application
  When I open the Veterinarians page
  Then the Veterinarians page should be displayed
  And at least one veterinarian should be listed