import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { AuthService } from "../../services/auth.service";
import { ReactiveFormsModule } from "@angular/forms";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RegisterComponent } from "./register.component";
import { Router } from "@angular/router";
import { expect } from '@jest/globals';
import { getByTestId } from '@testing-library/angular';
import '@testing-library/jest-dom';
import { RouterTestingModule } from "@angular/router/testing";

describe('RegisterComponent Integration Test Suites', () => {
    let component: RegisterComponent;
    let fixture: ComponentFixture<RegisterComponent>;
    let authService: AuthService;
    let controller: HttpTestingController;
    let router: Router;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
          declarations: [RegisterComponent],
          imports: [
            RouterTestingModule.withRoutes([
                { path: '**', component: RegisterComponent }
            ]),
            BrowserAnimationsModule,
            HttpClientTestingModule,
            ReactiveFormsModule,  
            MatCardModule,
            MatFormFieldModule,
            MatIconModule,
            MatInputModule
          ],
          providers: [AuthService]
        })
          .compileComponents();
    
        authService = TestBed.inject(AuthService);
        controller = TestBed.inject(HttpTestingController);
        router = TestBed.inject(Router);
        
        fixture = TestBed.createComponent(RegisterComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
      });

      it(`should navigate to '/login' when it's OK`, () => {
        component.form.controls.email.setValue('test@test.com');
        component.form.controls.firstName.setValue('firstName');
        component.form.controls.lastName.setValue('lastName');
        component.form.controls.password.setValue('123456');

        let spyRouter = jest.spyOn(router, 'navigate');
        component.submit();

        const request = controller.expectOne({ method: 'POST', url: 'api/auth/register' });
        request.flush('');
        controller.verify();
        fixture.detectChanges();
        
        expect(spyRouter).toHaveBeenCalledWith(['/login']);
      });

      it(`should display an error message when an error is returned`, () => {
        component.form.controls.email.setValue('test@test.com');
        component.form.controls.firstName.setValue('firstName');
        component.form.controls.lastName.setValue('lastName');
        component.form.controls.password.setValue('12');

        component.submit();

        const request = controller.expectOne({ method: 'POST', url: 'api/auth/register' });
        request.flush('', { status: 400, statusText: 'Bad request' });
        controller.verify();
        fixture.detectChanges();

        expect(getByTestId(document.body, 'register-error')).toBeInTheDocument();
        expect(getByTestId(document.body, 'register-error')).toHaveTextContent('An error occurred');
      });
});