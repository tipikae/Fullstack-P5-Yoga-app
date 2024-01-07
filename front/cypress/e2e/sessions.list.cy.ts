describe('Sessions list spec', () => {
    
    it('should display a list of sessions', () => {
        cy.fakeUserLoginRedirectToFullListSession()

        cy.get('.items .item mat-card-title').should('have.length', 2)
        cy.get('.items .item mat-card-title').eq(0).should('contain.text', 'session1')
    });

    it('should display an empty list of sessions', () => {
        cy.fakeLoginRedirectToEmptyListSession()

        cy.get('.items .item mat-card-title').should('not.exist')
    });
});