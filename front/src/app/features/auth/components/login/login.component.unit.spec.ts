import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { of } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const mockAuthService = {
    login: jest.fn().mockReturnValue(of())
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        SessionService,
        { provide: AuthService, useValue: mockAuthService }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(`form should be invalid when field is missing or incorrect`, () => {
    // email missing
    component.form.controls.email.setValue(null);
    component.form.controls.password.setValue('123456');
    expect(component.form.valid).toBeFalsy();

    // email incorrect
    component.form.controls.email.setValue('test.com');
    component.form.controls.password.setValue('123456');
    expect(component.form.valid).toBeFalsy();

    // password missing
    component.form.controls.email.setValue('test@test.com');
    component.form.controls.password.setValue(null);
    expect(component.form.valid).toBeFalsy();

    // password incorrect
    component.form.controls.email.setValue('test@test.com');
    component.form.controls.password.setValue('12');
    expect(component.form.valid).toBeFalsy();
  });

  it(`form should be valid when fields are correct`, () => {
    component.form.controls.email.setValue('test@test.com');
    component.form.controls.password.setValue('123456');
    expect(component.form.valid).toBeTruthy();
  });

  it(`should call 'authService.login()' when 'submit()' is called`, () => {
    component.submit();
    expect(mockAuthService.login).toHaveBeenCalled();
  });
});
