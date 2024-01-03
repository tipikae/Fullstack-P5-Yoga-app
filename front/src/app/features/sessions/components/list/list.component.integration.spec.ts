import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ListComponent } from "./list.component";
import { SessionService } from "src/app/services/session.service";
import { SessionApiService } from "../../services/session-api.service";
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { expect } from '@jest/globals';
import { getAllByTestId, queryAllByTestId, queryByTestId } from "@testing-library/angular";
import '@testing-library/jest-dom';
import { SessionInformation } from "src/app/interfaces/sessionInformation.interface";
import { Session } from "../../interfaces/session.interface";
import { RouterTestingModule } from "@angular/router/testing";

describe('ListComponent Integration Test Suites', () => {
    let component: ListComponent;
    let fixture: ComponentFixture<ListComponent>;
    let controller: HttpTestingController;
    let sessionService: SessionService

    const sessionInformation: SessionInformation = {
        token: 'token',
        type: 'type',
        id: 1,
        username: 'username',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
    }

    const sessions: Session[] = [
        {
            id: 1,
            name: 'session1',
            description: 'desc1',
            date: new Date(),
            teacher_id: 1,
            users: [1],
            createdAt: new Date(),
            updatedAt: new Date()
        },
        {
            id: 2,
            name: 'session2',
            description: 'desc2',
            date: new Date(),
            teacher_id: 2,
            users: [2],
            createdAt: new Date(),
            updatedAt: new Date()
        }
    ]

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [ListComponent],
            imports: [
                HttpClientTestingModule,
                RouterTestingModule,
                MatCardModule,
                MatIconModule
            ],
            providers: [
                SessionService,
                SessionApiService
            ]
        }).compileComponents();

        controller = TestBed.inject(HttpTestingController);
        sessionService = TestBed.inject(SessionService);

        sessionService.logIn(sessionInformation);


        fixture = TestBed.createComponent(ListComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should display sessions information when list is not empty', () => {
        const request = controller.expectOne({ method: 'GET', url: 'api/session' });
        request.flush(sessions);
        controller.verify();
        fixture.detectChanges();

        expect(queryAllByTestId(document.body, 'session-name')[0]).toBeInTheDocument();
        expect(getAllByTestId(document.body, 'session-name')[0]).toHaveTextContent('session1');
    });

    it(`should display 'edit' btn when user is admin`, () => {
        const request = controller.expectOne({ method: 'GET', url: 'api/session' });
        request.flush(sessions);
        controller.verify();
        fixture.detectChanges();

        expect(queryAllByTestId(document.body, 'admin-edit-btn')[0]).toBeInTheDocument();
        expect(getAllByTestId(document.body, 'admin-edit-btn')[0]).toHaveTextContent('Edit');
    });

    it(`should not display 'edit' btn when user is not admin`, () => {
        sessionService.logOut();
        sessionInformation.admin = false;
        sessionService.logIn(sessionInformation);

        const request = controller.expectOne({ method: 'GET', url: 'api/session' });
        request.flush(sessions);
        controller.verify();
        fixture.detectChanges();

        expect(queryByTestId(document.body, 'admin-edit-btn')).toBeNull();
    });
});