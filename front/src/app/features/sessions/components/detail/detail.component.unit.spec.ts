import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of()),
    delete: jest.fn().mockReturnValue(of()),
    participate: jest.fn().mockReturnValue(of()),
    unParticipate: jest.fn().mockReturnValue(of())
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ],
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    component.sessionId = '1';
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(`should call 'window.history.back()' when 'back()' is called`, () => {
    jest.spyOn(window.history, 'back');
    component.back();
    expect(window.history.back).toHaveBeenCalled();
  });

  it(`should call 'sessionApiService.delete()' when 'delete()' is called`, () => {
    component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalled();
  });

  it(`should call 'sessionApiService.participate()' when 'participate()' is called`, () => {
    component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalled();
  });

  it(`should call 'sessionApiService.unParticipate()' when 'unParticipate()' is called`, () => {
    component.unParticipate();
    expect(mockSessionApiService.unParticipate).toHaveBeenCalled();
  });
});

