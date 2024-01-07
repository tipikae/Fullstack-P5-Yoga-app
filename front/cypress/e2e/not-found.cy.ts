describe('NotFound spec', () => {
    it(`should display 'Page not found !' when page is not found`, () => {
        cy.visit('unknown')

        cy.get('h1').should('have.text', 'Page not found !')
    });
})