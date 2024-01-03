import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { expect } from '@jest/globals';
import { getByTestId, queryByTestId } from "@testing-library/angular";
import '@testing-library/jest-dom';
import { SessionService } from "src/app/services/session.service";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { SessionInformation } from "src/app/interfaces/sessionInformation.interface";
import { SessionApiService } from "../../services/session-api.service";
import { DetailComponent } from "./detail.component";
import { Session } from "../../interfaces/session.interface";
import { RouterTestingModule } from "@angular/router/testing";
import { TeacherService } from "src/app/services/teacher.service";
import { FormBuilder } from "@angular/forms";
import { MatSnackBarModule } from "@angular/material/snack-bar";

describe('DetailComponent IntegrationTest Suites', () => {
    let component: DetailComponent;
    let fixture: ComponentFixture<DetailComponent>;
    let controller: HttpTestingController;
    let sessionService: SessionService;

    const sessionInformation: SessionInformation = {
        token: 'token',
        type: 'type',
        id: 1,
        username: 'username',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
    }

    const session: Session = {
        id: 1,
        name: 'session1',
        description: 'desc1',
        date: new Date(),
        teacher_id: 1,
        users: [1],
        createdAt: new Date(),
        updatedAt: new Date()
    }

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [DetailComponent],
            imports: [
                HttpClientTestingModule,
                RouterTestingModule,
                MatCardModule,
                MatIconModule,
                MatSnackBarModule
            ],
            providers: [
                SessionService,
                SessionApiService,
                TeacherService,
                FormBuilder
            ]
        }).compileComponents();

        controller = TestBed.inject(HttpTestingController);
        sessionService = TestBed.inject(SessionService);

        sessionService.logIn(sessionInformation);

        fixture = TestBed.createComponent(DetailComponent);
        component = fixture.componentInstance;

        component.sessionId = '1';
        fixture.detectChanges();

        let request = controller.expectOne({ method: 'GET', url: 'api/session/' + component.sessionId });
        request.flush(session);
        fixture.detectChanges();
    });

    it('should display session information', () => {
        expect(getByTestId(document.body, 'session-name')).toHaveTextContent('Session1');
    });

    it(`should display 'delete' button when user is admin`, () => {
        expect(queryByTestId(document.body, 'admin-delete-btn')).toBeInTheDocument();
        expect(getByTestId(document.body, 'admin-delete-btn')).toHaveTextContent('Delete');
    });
});