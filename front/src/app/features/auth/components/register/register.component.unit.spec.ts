import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { of } from 'rxjs';
import { AuthService } from '../../services/auth.service';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  const mockAuthService = {
    register: jest.fn().mockReturnValue(of())
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('form should be invalid when fileds are missing or incorrect', () => {
    // email missing
    component.form.controls.email.setValue(null);
    component.form.controls.firstName.setValue('test');
    component.form.controls.lastName.setValue('TEST');
    component.form.controls.password.setValue('123456');
    expect(component.form.valid).toBeFalsy();

    // email incorrect
    component.form.controls.email.setValue('test.com');
    component.form.controls.firstName.setValue('test');
    component.form.controls.lastName.setValue('TEST');
    component.form.controls.password.setValue('123456');
    expect(component.form.valid).toBeFalsy();

    // firstname missing
    component.form.controls.email.setValue('test@test.com');
    component.form.controls.firstName.setValue(null);
    component.form.controls.lastName.setValue('TEST');
    component.form.controls.password.setValue('123456');
    expect(component.form.valid).toBeFalsy();

    // firstname incorrect
    component.form.controls.email.setValue('test@test.com');
    component.form.controls.firstName.setValue('te');
    component.form.controls.lastName.setValue('TEST');
    component.form.controls.password.setValue('123456');
    expect(component.form.valid).toBeFalsy();

    // lastname missing
    component.form.controls.email.setValue('test@test.com');
    component.form.controls.firstName.setValue('test');
    component.form.controls.lastName.setValue(null);
    component.form.controls.password.setValue('123456');
    expect(component.form.valid).toBeFalsy();

    // lastname incorrect
    component.form.controls.email.setValue('test@test.com');
    component.form.controls.firstName.setValue('test');
    component.form.controls.lastName.setValue('TE');
    component.form.controls.password.setValue('123456');
    expect(component.form.valid).toBeFalsy();

    // password missing
    component.form.controls.email.setValue('test@test.com');
    component.form.controls.firstName.setValue('test');
    component.form.controls.lastName.setValue('TEST');
    component.form.controls.password.setValue(null);
    expect(component.form.valid).toBeFalsy();

    // password incorrect
    component.form.controls.email.setValue('test@test.com');
    component.form.controls.firstName.setValue('test');
    component.form.controls.lastName.setValue('TEST');
    component.form.controls.password.setValue('12');
    expect(component.form.valid).toBeFalsy();
  });

  it('form shoulb be valid when fields are correct', () => {
    component.form.controls.email.setValue('test@test.com');
    component.form.controls.firstName.setValue('test');
    component.form.controls.lastName.setValue('TEST');
    component.form.controls.password.setValue('123456');
    expect(component.form.valid).toBeTruthy();
  });

  it(`should call 'authService.register()' when 'submit' is called`, () => {
    component.submit();
    expect(mockAuthService.register).toHaveBeenCalled();
  });
});
