declare namespace Cypress {
    interface Chainable<Subject = any> {
        fakeAdminLoginRedirectToFullListSession(): typeof fakeAdminLoginRedirectToFullListSession;
        fakeUserLoginRedirectToFullListSession(): typeof fakeUserLoginRedirectToFullListSession;
        fakeLoginRedirectToEmptyListSession(): typeof fakeLoginRedirectToEmptyListSession;
    }
}

/**
 * Simulates an admin login and redirects to sessions page of 2 sessions.
 */
function fakeAdminLoginRedirectToFullListSession() {
    cy.visit('/login')

    cy.intercept('POST', 'api/auth/login', {
        body: {
            token: 'jwt',
            type: 'Bearer',
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: true
        }
    })

    cy.intercept(
        'GET',
        'api/session',
        {
            statusCode: 200,
            body: [
                {
                    id: 1,
                    name: 'session1',
                    description: 'desc1',
                    date: new Date(),
                    teacher_id: 1,
                    users: [],
                    createdAt: new Date(),
                    updatedAt: new Date()
                },
                {
                    id: 2,
                    name: 'session2',
                    description: 'desc2',
                    date: new Date(),
                    teacher_id: 2,
                    users: [],
                    createdAt: new Date(),
                    updatedAt: new Date()
                }
            ]
        }
    ).as('full-list')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
}

/**
 * Simulates a user login and redirects to sessions page of 2 sessions.
 */
function fakeUserLoginRedirectToFullListSession() {
    cy.visit('/login')

    cy.intercept('POST', 'api/auth/login', {
        body: {
            token: 'jwt',
            type: 'Bearer',
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: false
        }
    })

    cy.intercept(
        'GET',
        'api/session',
        {
            statusCode: 200,
            body: [
                {
                    id: 1,
                    name: 'session1',
                    description: 'desc1',
                    date: new Date(),
                    teacher_id: 1,
                    users: [],
                    createdAt: new Date(),
                    updatedAt: new Date()
                },
                {
                    id: 2,
                    name: 'session2',
                    description: 'desc2',
                    date: new Date(),
                    teacher_id: 2,
                    users: [],
                    createdAt: new Date(),
                    updatedAt: new Date()
                }
            ]
        }
    ).as('full-list')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
}

/**
 * Simulates a login and redirects to an empty sessions page.
 */
function fakeLoginRedirectToEmptyListSession() {
    cy.visit('/login')

    cy.intercept('POST', 'api/auth/login', {
        body: {
            token: 'jwt',
            type: 'Bearer',
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: true
        }
    })

    cy.intercept(
        'GET',
        'api/session',
        {
            statusCode: 200,
            body: []
        }
    ).as('empty-list')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
}

Cypress.Commands.add("fakeAdminLoginRedirectToFullListSession", fakeAdminLoginRedirectToFullListSession)
Cypress.Commands.add("fakeUserLoginRedirectToFullListSession", fakeUserLoginRedirectToFullListSession)
Cypress.Commands.add("fakeLoginRedirectToEmptyListSession", fakeLoginRedirectToEmptyListSession)
