import { ComponentFixture, TestBed } from "@angular/core/testing";
import { FormComponent } from "./form.component";
import { SessionService } from "src/app/services/session.service";
import { SessionApiService } from "../../services/session-api.service";
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { SessionInformation } from "src/app/interfaces/sessionInformation.interface";
import { Session } from "../../interfaces/session.interface";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { FormBuilder, ReactiveFormsModule } from "@angular/forms";
import { RouterTestingModule } from "@angular/router/testing";
import { TeacherService } from "src/app/services/teacher.service";
import { expect } from '@jest/globals';
import { ActivatedRoute, Router } from "@angular/router";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatCardModule } from "@angular/material/card";
import { MatInputModule } from "@angular/material/input";
import { MatSelectModule } from "@angular/material/select";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";

describe('FormComponent Integration Test Suites', () => {
    let component: FormComponent;
    let fixture: ComponentFixture<FormComponent>;
    let sessionService: SessionService;
    let sessionApiService: SessionApiService;
    let controller: HttpTestingController;
    let router: Router;
    let id_session = 1;

    const sessionInformation: SessionInformation = {
        token: 'token',
        type: 'type',
        id: 1,
        username: 'username',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
    }

    const newSession = {
        name: 'session1',
        description: 'desc1',
        date: new Date(),
        teacher_id: 1
    }

    const session: Session = {
        id: id_session,
        name: 'session1',
        description: 'desc1',
        date: new Date(),
        teacher_id: 1,
        users: [1],
        createdAt: new Date(),
        updatedAt: new Date()
    }

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [FormComponent],
            imports: [
                HttpClientTestingModule,
                MatSnackBarModule,
                RouterTestingModule.withRoutes([
                    { path: '**', component: FormComponent}
                ]),
                MatFormFieldModule,
                MatIconModule,
                MatCardModule,
                MatInputModule,
                ReactiveFormsModule,
                MatSelectModule,
                NoopAnimationsModule
            ],
            providers: [
                FormBuilder,
                SessionApiService,
                SessionService,
                TeacherService,
                { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: (id: number) => { return id_session } } } } }
            ]
        });

        controller = TestBed.inject(HttpTestingController);
        sessionService = TestBed.inject(SessionService);
        sessionApiService = TestBed.inject(SessionApiService);
        router = TestBed.inject(Router);

        sessionService.logIn(sessionInformation);

        fixture = TestBed.createComponent(FormComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it(`should call 'sessionApiService.detail()' when actual route contains 'update'`, () => {
        expect(component.onUpdate).toBeFalsy();

        jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update');
        jest.spyOn(sessionApiService, 'detail');

        component.ngOnInit();

        const request = controller.expectOne({ method: 'GET', url: 'api/session/' + id_session });
        request.flush(session);

        expect(component.onUpdate).toBeTruthy();
        expect(sessionApiService.detail).toHaveBeenCalledWith(id_session);
    });

    it(`should call 'sessionApiService.create()' when 'submit()' is called and actual route does not contain 'update'`, () => {
        jest.spyOn(sessionApiService, 'create');
        component.sessionForm?.controls["name"].setValue(newSession.name);
        component.sessionForm?.controls["date"].setValue(newSession.date);
        component.sessionForm?.controls["description"].setValue(newSession.description);
        component.sessionForm?.controls["teacher_id"].setValue(newSession.teacher_id);

        component.submit();

        const request = controller.expectOne({ method: 'POST', url: 'api/session' });
        request.flush(session);
        
        expect(sessionApiService.create).toHaveBeenCalledWith(newSession);
    });

    it(`should call 'sessionApiService.update()' when 'submit()' is called and actual route contains 'update'`, () => {
        jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update');
        jest.spyOn(sessionApiService, 'update');
        component.sessionForm?.controls["name"].setValue(newSession.name);
        component.sessionForm?.controls["date"].setValue(newSession.date);
        component.sessionForm?.controls["description"].setValue(newSession.description);
        component.sessionForm?.controls["teacher_id"].setValue(newSession.teacher_id);

        component.ngOnInit();
        component.submit();

        const request = controller.expectOne({ method: 'PUT', url: 'api/session/'  + id_session});
        request.flush(session);

        expect(sessionApiService.update).toHaveBeenCalledWith(id_session, newSession);
    });
});