describe('Session form spec', () => {
    it(`should display 'create' and 'edit' button when user is admin`, () => {
        // login
        cy.fakeAdminLoginRedirectToFullListSession()

        // sessions list page
        cy.contains('button', 'Create')
        cy.get('.item').each((element, index, $list) => {
            cy.wrap(element).contains('button', 'Edit')
        })
    });

    it(`should display empty form when 'create' is clicked`, () => {
        // login
        cy.fakeAdminLoginRedirectToFullListSession()

        // sessions list page
        cy.intercept('GET', 'api/teacher',
            {
                statusCode: 200,
                body: [
                    {
                        id: 1,
                        lastName: 'teacher-lastName',
                        firstName: 'teacher-firstName',
                        createdAt: new Date(),
                        updatedAt: new Date()
                    }
                ]
            })

        cy.contains('button', 'Create').click()

        // create form
        cy.url().should('include', '/create')
        cy.get('input[formControlName=name]').should('have.text', '')
    });

    it(`should create session and redirect to sessions page when 'submit is clicked'`, () => {
        // login
        cy.fakeAdminLoginRedirectToFullListSession()

        // sessions list page
        cy.intercept('GET', 'api/teacher', {
            statusCode: 200,
            body: [
                {
                    id: 1,
                    lastName: 'teacher-lastName',
                    firstName: 'teacher-firstName',
                    createdAt: new Date(),
                    updatedAt: new Date()
                }
            ]
        }).as('teacher-all')

        cy.contains('button', 'Create').click()

        // create form
        cy.intercept({method: 'POST', url: 'api/session'}, [])

        cy.get('input[formControlName=name]').type('session-name')
        cy.get('input[formControlName=date]').type('2024-01-06')
        cy.get('mat-select[formControlName=teacher_id]').click()
            .get('mat-option').contains('teacher-firstName teacher-lastName').click()
        cy.get('textarea[formControlName=description]').type('session-description')
        cy.get('button[type=submit]').click()

        // sessions list page
        cy.url().should('include', '/sessions')
    });

    it(`should update session and redirect to sessions when 'edit' is clicked`, () => {
        // login
        cy.fakeAdminLoginRedirectToFullListSession()

        // sessions list page
        cy.intercept('GET', 'api/teacher', {
            statusCode: 200,
            body: [
                {
                    id: 1,
                    lastName: 'teacher-lastName',
                    firstName: 'teacher-firstName',
                    createdAt: new Date(),
                    updatedAt: new Date()
                }
            ]
        }).as('teacher-all')

        cy.intercept('GET', 'api/session/1', {
            statusCode: 200,
            body: {
                id: 1,
                name: 'session1',
                description: 'desc1',
                date: new Date(),
                teacher_id: 1,
                users: [],
                createdAt: new Date(),
                updatedAt: new Date()
            }
        }).as('detail')

        cy.contains('button', 'Edit').eq(0).click()

        // update form
        cy.intercept({method: 'PUT', url: 'api/session/1'}, []).as('update')

        cy.get('input[formControlName="name"]').invoke('val').should('equal', 'session1')
        cy.get('input[formControlName=name]').type('session1-updated')
        cy.get('button[type=submit]').click()

        // sessions list page
        cy.url().should('include', '/sessions')
    });
});