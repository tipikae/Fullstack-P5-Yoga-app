import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';
import { getByTestId } from '@testing-library/angular';
import '@testing-library/jest-dom';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { User } from 'src/app/interfaces/user.interface';
import { tap } from 'rxjs';

describe('MeComponent Integration Test Suites', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let sessionService: SessionService;
  let userService: UserService;
  let controller: HttpTestingController;

  const sessionInformation: SessionInformation = {
    token: 'token',
    type: 'type',
    id: 1,
    username: 'test',
    firstName: 'firstName',
    lastName: 'lastName',
    admin: false
  }

  const user: User = {
    id: 1,
    email: 'test@test.com',
    lastName: 'test',
    firstName: 'Test',
    admin: false,
    password: '123456',
    createdAt: new Date(),
    updatedAt: new Date()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientTestingModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        SessionService,
        UserService
      ],
    }).compileComponents();

    sessionService = TestBed.inject(SessionService);
    userService = TestBed.inject(UserService);
    controller = TestBed.inject(HttpTestingController);
  });

  it(`should display user information`, () => {
    sessionService.logIn(sessionInformation);
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    const request = controller.expectOne({ method: 'GET', url: 'api/user/' + sessionInformation.id });
    request.flush(user);
    controller.verify();
    fixture.detectChanges();

    expect(sessionService.isLogged).toBeTruthy();
    expect(getByTestId(document.body, 'user-fullname')).toHaveTextContent('Name: Test TEST');
  });

  it(`should display admin information`, () => {
    sessionInformation.admin = true;
    let admin = user;
    admin.firstName = 'admin';
    admin.admin = true;

    sessionService.logIn(sessionInformation);
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    const request = controller.expectOne({ method: 'GET', url: 'api/user/' + sessionInformation.id });
    request.flush(admin);
    controller.verify();
    fixture.detectChanges();

    expect(getByTestId(document.body, 'user-fullname')).toHaveTextContent('Name: admin TEST');
    expect(getByTestId(document.body, 'user-is-admin')).toBeInTheDocument();
  });

  it(`should logout user when user is deleted`, () => {
    sessionService.logIn(sessionInformation);
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.delete();

    const request = controller.expectOne({ method: 'DELETE', url: 'api/user/' + sessionInformation.id });
    request.flush('OK');
    fixture.detectChanges();

    sessionService.$isLogged().pipe(
      tap((isLogged) => {
        expect(isLogged).toBeFalsy();
      })
    );
  });
});
