import { mount } from 'cypress/svelte'
import ForecastsSection from '../../src/lib/organism/ForecastsSection.svelte'

  
// Cypress test
describe('Forecast display', () => {

    it('should display meteo for given coordinates', () => {
        mount(ForecastsSection, { 
            props : { 
                place : {
                    country : "test", 
                    city : "test", 
                    street : "test", 
                    number : "0", 
                    postalCode : "0000", 
                    lat : 52.06426, 
                    lon : 41.66793
                } 
            } 
        });

        cy.get('#forecast').should('be.visible');
    });

    it('should display error for wrong coordinates', () => {
        mount(ForecastsSection, { 
            props : { 
                place : {
                    country : "test", 
                    city : "test", 
                    street : "test", 
                    number : "0", 
                    postalCode : "0000", 
                    lat : 99999, 
                    lon : 99999
                } 
            } 
        });

        cy.contains("Erreur lors du chargement de la météo").should('be.visible');
    });


});