import { mount } from 'cypress/svelte'

describe('Connect to app', () => {

  it('login with valid credentials', () => {
    cy.request({
      method: 'POST', 
      url : 'auth/login', 
      body : { 
        mail : "test1234@web.com", 
        password : "test1234" 
      }
    })
    .its('body')
    .should('not.be.null');
  });

  it('login with invalid credentials', () => {
    cy.request({
      method: 'POST',
      url: 'auth/login',
      body: {
        mail: 'testError@web.com',
        password: 'testError',
      },
      failOnStatusCode: false,
    })
    .its('status')
    .should('equal', 400)
  });

});
