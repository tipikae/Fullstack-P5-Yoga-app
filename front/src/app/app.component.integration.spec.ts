import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { tap } from 'rxjs';
import { Router } from '@angular/router';

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let app: AppComponent;
  let sessionService: SessionService;
  let router: Router;

  const sessionInformation = {
    token: 'token',
    type: 'type',
    id: 1,
    username: 'test',
    firstName: 'firstName',
    lastName: 'lastName',
    admin: true
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: '**', component: AppComponent }
        ]),
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        SessionService
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);

    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });

  it(`should return true when a user is logged in`, () => {
    sessionService.logIn(sessionInformation);
    app.$isLogged().pipe(
      tap((isLogged) => {
        expect(isLogged).toBeTruthy();
      })
    );
  });

  it(`should return false when a user is logged out`, () => {
    sessionService.logOut();
    app.$isLogged().pipe(
      tap((isLogged) => {
        expect(isLogged).toBeFalsy();
      })
    );
  });

  it(`should call 'sessionService.logOut()' when 'logout()' is called`, () => {
    jest.spyOn(sessionService, 'logOut');
    jest.spyOn(router, 'navigate').mockReturnValue(new Promise(() => { return true;}));
    app.logout();
    expect(sessionService.logOut).toHaveBeenCalled();
  });
});
