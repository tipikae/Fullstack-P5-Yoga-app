import { ComponentFixture, TestBed } from "@angular/core/testing";
import { LoginComponent } from "./login.component";
import { AuthService } from "../../services/auth.service";
import { SessionService } from "src/app/services/session.service";
import { RouterTestingModule } from "@angular/router/testing";
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { expect } from '@jest/globals';
import { getByTestId } from '@testing-library/angular';
import '@testing-library/jest-dom';

describe('LoginComponent Integration Test Suites', () => {
    let component: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;
    let authService: AuthService;
    let sessionService: SessionService;
    let controller: HttpTestingController;

    const sessionInformation = {
        token: 'token',
        type: 'type',
        id: 1,
        username: 'username',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
    }

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [LoginComponent],
            imports: [
                RouterTestingModule,
                HttpClientTestingModule,
                BrowserAnimationsModule,
                MatCardModule,
                MatIconModule,
                MatFormFieldModule,
                MatInputModule,
                ReactiveFormsModule
            ],
            providers: [
                AuthService,
                SessionService
            ]
        }).compileComponents();

        authService = TestBed.inject(AuthService);
        sessionService = TestBed.inject(SessionService);
        controller = TestBed.inject(HttpTestingController);

        fixture = TestBed.createComponent(LoginComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it(`should log in user when 'submit()' is called and credentials are correct`, () => {
        component.form.controls.email.setValue('test@test.com');
        component.form.controls.password.setValue('123456');

        component.submit();

        const request = controller.expectOne({ method: 'POST', url: 'api/auth/login' });
        request.flush(sessionInformation);
        controller.verify();

        expect(sessionService.isLogged).toBeTruthy();
    });

    it(`should display error message when login error is returned`, () => {
        component.form.controls.email.setValue('test@test.com');
        component.form.controls.password.setValue('12');

        component.submit();

        const request = controller.expectOne({ method: 'POST', url: 'api/auth/login' });
        request.flush('', { status: 401, statusText: 'unauthorized' });
        controller.verify();
        fixture.detectChanges();

        expect(getByTestId(document.body, 'login-error')).toBeInTheDocument();
        expect(getByTestId(document.body, 'login-error')).toHaveTextContent('An error occurred');
    });
});