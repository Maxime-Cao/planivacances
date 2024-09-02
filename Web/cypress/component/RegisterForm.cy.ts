import { mount } from 'cypress/svelte'
import MockRegisterPage from './MockRegisterPage.svelte'

describe('Contact form tests', () => {
  beforeEach(() => {
    mount(MockRegisterPage);
  });

  it('submit with wrong password repeat', () => {
    // Type name
    cy.get('input[name="name"]').type('test');

    // Type surname
    cy.get('input[name="surname"]').type('test');

    // Type email
    cy.get('input[name="email"]').type('test1234@web.com');
  
    // Type password
    cy.get('input[name="password"]').type('test1234');

    // Type passwordRepeat
    cy.get('input[name="passwordRepeat"]').type('wrongPassword');

    // Submit the form
    cy.get('form').submit();

    // Assert that the error message is displayed
    cy.contains("Problème de correspondance des mots de passe").should('be.visible');
  });

});