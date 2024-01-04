describe('Register spec', () => {
    it('Register successfull', () => {
        cy.visit('/register')

        cy.intercept('POST', '/api/auth/register', {
            statusCode: 200,
            body: {
                message: 'User registered successfully!'
            }
        })

        cy.get('input[formControlName=firstName]').type("firstName")
        cy.get('input[formControlName=lastName]').type("lastName")
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.url().should('include', '/login')
    });

    it('Register failed', () => {
        cy.visit('/register')

        cy.intercept('POST', '/api/auth/register', {
            statusCode: 400,
            body: {
                message: 'Error: Email is already taken!'
            }
        })

        cy.get('input[formControlName=firstName]').type("firstName")
        cy.get('input[formControlName=lastName]').type("lastName")
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.contains('An error occurred')
    });
})