describe('Guards spec', () =>{
    it(`should redirect to login when not logged in`, () => {
        cy.visit('/sessions')

        cy.url().should('include', 'login')
    });
})