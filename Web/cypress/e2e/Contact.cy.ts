import { mount } from 'cypress/svelte'

describe('Send Contact Mail', () => {

  it('Send valid contact mail', () => {
    cy.request({
        method : 'POST', 
        url : 'users/admin/message', 
        body : { 
            email : "test1234@web.com", 
            subject : "sujet du test cypress API", 
            message : "message du test" 
        }
    })
    .its('status')
    .should('equal', 200);
  });

  it('send invalid contact mail', () => {
    cy.request({
        method : 'POST', 
        url : 'users/admin/message', 
        body : { 
            email : "",
            subject : "sujet du test cypress API", 
            message : "message du test" 
        }
    })
    .its('status')
    .should('equal', 400);
  });

});