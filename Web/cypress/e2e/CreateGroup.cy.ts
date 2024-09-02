import { mount } from 'cypress/svelte'
import { app } from "../../src/service/FirebaseApp";
import { 
    getAuth, 
    signInWithCustomToken
} from "firebase/auth";

const auth = getAuth(app);

describe('Create group', () => {
    beforeEach(() => {
        cy.request({
            method: 'POST', 
            url : 'auth/login', 
            body : { 
              mail : "test1234@web.com", 
              password : "test1234" 
            }
        })
        .its('body')
        .should('not.be.null')
        .as('customToken')
    });

    it('Send valid group create', () => {
        cy.get('@customToken').then((customToken) => {
            cy.wrap(signInWithCustomToken(auth, customToken as unknown as string)).then(credentials => {
                cy.wrap(credentials.user.getIdToken(false)).then(token => {
                    cy.log(`idToken : ${token}`);
                    cy.request({
                        method : 'POST',
                        url : 'group',
                        body : {
                            gid : "null", 
                            groupName : "Test Cypress", 
                            description : "groupe test",
                            startDate : "2023-10-29T11:01:00.000",
                            endDate : "2023-11-29T11:01:00.000",
                            place : {
                                country : "pays test",
                                city : "test",
                                street : "test street",
                                number : "42",
                                postalCode : "4242",
                                lat : 0,
                                lon : 0
                            },
                            owner : "null"
                        },
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${token}`
                        }
                    })
                    .its('body')
                    .should('not.be.null')
                    .as('gid');

                    cy.get('@gid').then((gid) => {
                        cy.request({
                            method: 'GET', 
                            url : `group/${gid}`,
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': `Bearer ${token}`
                            } 
                        })
                        .its('status')
                        .should('equal', 200)
            
                        cy.request({
                            method: 'DELETE', 
                            url : `group/${gid}`,
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

    
});