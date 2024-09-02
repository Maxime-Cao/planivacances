import { mount } from 'cypress/svelte'
import { app } from "../../src/service/FirebaseApp";
import { 
    getAuth, 
    signInWithCustomToken
} from "firebase/auth";

const auth = getAuth(app);

describe('Create user', () => {
    beforeEach(() => {
        cy.request({
            method: 'POST', 
            url : 'auth/register', 
            body : { 
                username : 'testCypress',
                mail : 'testCypress@web.com', 
                password : 'test1234'
            }
        })
        .its('body')
        .should('not.be.null')
        .as('customToken')
    });

    it('delete user', () => {
        cy.get('@customToken').then((customToken) => {
            cy.wrap(signInWithCustomToken(auth, customToken as unknown as string)).then(credentials => {
                cy.wrap(credentials.user.getIdToken(false)).then(token => {
                    cy.log(`idToken : ${token}`);
            
                    cy.request({
                        method: 'DELETE', 
                        url : `users`,
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${token}`
                        }
                    })
                    .its('status')
                    .should('equal', 200)
                });
            });

        });
    });
    
});