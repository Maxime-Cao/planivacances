import { mount } from 'cypress/svelte'
import MockContactPage from './MockContactPage.svelte'

describe('Contact form tests', () => {
  beforeEach(() => {
    mount(MockContactPage);
  });

  it('submit the form with valid data', () => {
    // Type email
    cy.get('input[name="email"]').type('test@example.com');
  
    // Type subject
    cy.get('input[name="subject"]').type('Test Subject');

    // Type message
    cy.get('textarea[name="message"]').type('This is a test message.');

    // Submit the form
    cy.get('form').submit();

    // Assert that the form submission is successful (modify as needed)
    cy.url().should('include', '/'); // Assuming a success page URL
    cy.contains('Mail contact envoyé').should('be.visible');
  });

  it('submit with empty data', () => {
    // Leave email field empty
    // ...

    // Type subject
    cy.get('input[name="subject"]').type('Test Subject');

    // Type message
    cy.get('textarea[name="message"]').type('This is a test message.');

    // Submit the form
    cy.get('form').submit();

    // Assert that the error message is displayed
    cy.contains("Veuillez renseigner tous les champs").should('be.visible');
  });

});