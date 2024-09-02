import { mount } from 'cypress/svelte'
import InputPassword from '../../src/lib/molecule/InputPasswordWithToggle.svelte'

  
// Cypress test
describe('Password Visibility', () => {
    beforeEach(() => {
        mount(InputPassword);
    });

    it('should check if password is visible', () => {
        // Toggle password visibility and check if the input type changes
        cy.get('.fa-solid').click();

        cy.get('#password-container Input').should('have.attr', 'type', 'text');
    });

    it('should check if password is not visible', () => {
        // Type a password and check if the input is not visible
        cy.get('#password-container Input').should('have.attr', 'type', 'password');
    });
});
  