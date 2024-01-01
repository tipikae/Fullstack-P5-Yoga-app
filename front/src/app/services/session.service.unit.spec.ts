import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  const mockSessionInformation = {
    token: 'token',
    type: 'type',
    id: 1,
    username: 'test',
    firstName: 'firstName',
    lastName: 'lastName',
    admin: true
  }

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it(`should define user when 'logIn' is called`, () => {
    service.logIn(mockSessionInformation);
    expect(service.isLogged).toBeTruthy();
    expect(service.sessionInformation?.username).toEqual('test');
  });

  it(`should undefine user when 'logOut' is called`, () => {
    service.logIn(mockSessionInformation);
    service.logOut();
    expect(service.isLogged).toBeFalsy();
    expect(service.sessionInformation).toEqual(undefined);
  });
});
