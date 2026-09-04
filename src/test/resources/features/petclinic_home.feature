@ui @smoke @home_test
Feature: Petclinic home page

  Scenario: Main navigation is displayed
    Given I open the Petclinic application
    Then the Home and Owners navigation links should be displayed
    And the page heading should be "Welcome to Petclinic"

  @ui @smoke @test
  Scenario: Main navigation contains the expected links
    Given I open the Petclinic application
    Then the main navigation should contain:
      | Home          |
      | Owners        |
      | Veterinarians |
      | Pet Types     |
      | Specialties   |