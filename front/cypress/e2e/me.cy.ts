describe('Me spec', () => {
    it(`should display user information`, () => {
        // login
        cy.fakeUserLoginRedirectToFullListSession()

        cy.intercept('GET', 'api/user/1', {
            body: {
                id: 1,
                email: 'test@test.com',
                lastName: 'lastName',
                firstName: 'firstName',
                admin: false,
                password: '123456',
                createdAt: new Date(),
                updatedAt: new Date()
            }
        })

        cy.get('span[routerLink=me]').click()

        // Me page
        cy.get('p[data-testid=user-fullname]').should('have.text', 'Name: firstName LASTNAME')
    });

    it(`should display 'You are admin' when user is admin`, () => {
        // login
        cy.fakeAdminLoginRedirectToFullListSession()

        cy.intercept('GET', 'api/user/1', {
            body: {
                id: 1,
                email: 'test@test.com',
                lastName: 'lastName',
                firstName: 'firstName',
                admin: true,
                password: '123456',
                createdAt: new Date(),
                updatedAt: new Date()
            }
        })

        cy.get('span[routerLink=me]').click()

        // Me page
        cy.get('p[data-testid=user-is-admin]').should('have.text', 'You are admin')
    });
})