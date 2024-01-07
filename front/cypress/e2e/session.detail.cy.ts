describe('Session detail spec', () => {
    it('should display a session', () => {
        cy.fakeUserLoginRedirectToFullListSession();

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
            }
        ).as('session-detail')

        cy.intercept({ method: 'GET', url: 'api/teacher/1'}, []).as('teacher-detail')

        cy.contains('button', 'Detail').eq(0).click()

        cy.get('h1').should('have.text', 'Session1')
    });

    it( `should display 'participate' and not display 'delete' button when not admin`, () => {
        cy.fakeUserLoginRedirectToFullListSession()

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
            }
        ).as('session-detail')

        cy.intercept({ method: 'GET', url: 'api/teacher/1'}, []).as('teacher-detail')

        cy.contains('button', 'Detail').eq(0).click()

        cy.contains('button', 'Participate')
        cy.contains('button', 'Delete').should('not.exist')
    });

    it( `should not display 'participate' and display 'delete' button when is admin`, () => {
        cy.fakeAdminLoginRedirectToFullListSession()

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
            }
        ).as('session-detail')

        cy.intercept({ method: 'GET', url: 'api/teacher/1'}, []).as('teacher-detail')

        cy.contains('button', 'Detail').eq(0).click()

        cy.contains('button', 'Participate').should('not.exist')
        cy.contains('button', 'Delete')
    });
    
    it( `should display 'unParticipate' button when 'participate' is clicked`, () => {
        // login
        cy.fakeUserLoginRedirectToFullListSession()

        // sessions list page
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
            }
        ).as('session-detail1')

        cy.intercept({ method: 'GET', url: 'api/teacher/1'}, []).as('teacher-detail1')

        cy.contains('button', 'Detail').eq(0).click()

        // session detail page
        cy.intercept({method: 'POST', url: 'api/session/1/participate/1'}, []).as('participate')
        cy.intercept('GET', 'api/session/1', {
                statusCode: 200,
                body: {
                    id: 1,
                    name: 'session1',
                    description: 'desc1',
                    date: new Date(),
                    teacher_id: 1,
                    users: [1],
                    createdAt: new Date(),
                    updatedAt: new Date()
                }
            }
        ).as('session-detail2')
        cy.intercept({ method: 'GET', url: 'api/teacher/1'}, []).as('teacher-detail2')

        cy.contains('button', 'Participate')
        cy.contains('button', 'Participate').click()
        cy.contains('button', 'Do not participate')
    });
    
    it( `should display 'participate' button when 'Do not participate' is clicked`, () => {
        // login
        cy.fakeUserLoginRedirectToFullListSession()

        // sessions list page
        cy.intercept('GET', 'api/session/1', {
                statusCode: 200,
                body: {
                    id: 1,
                    name: 'session1',
                    description: 'desc1',
                    date: new Date(),
                    teacher_id: 1,
                    users: [1],
                    createdAt: new Date(),
                    updatedAt: new Date()
                }
            }
        ).as('session-detail1')

        cy.intercept({ method: 'GET', url: 'api/teacher/1'}, []).as('teacher-detail1')

        cy.contains('button', 'Detail').eq(0).click()

        // session detail page
        cy.intercept({method: 'DELETE', url: 'api/session/1/participate/1'}, []).as('unParticipate')
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
            }
        ).as('session-detail2')
        cy.intercept({ method: 'GET', url: 'api/teacher/1'}, []).as('teacher-detail2')

        cy.contains('button', 'Do not participate')
        cy.contains('button', 'Do not participate').click()
        cy.contains('button', 'Participate')
    });

    it(`should delete a session and redirect to /sessions when 'delete' is clicked`, () => {
        // login
        cy.fakeAdminLoginRedirectToFullListSession()

        // sessions list page
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
            }
        ).as('session-detail')

        cy.intercept({ method: 'GET', url: 'api/teacher/1'}, []).as('teacher-detail')

        cy.contains('button', 'Detail').eq(0).click()

        // session detail page
        cy.intercept({method: 'DELETE', url: 'api/sessions/1'}, []).as('delete-session')
        cy.contains('button', 'Delete').click()

        cy.url().should('include', '/sessions')
    });
});