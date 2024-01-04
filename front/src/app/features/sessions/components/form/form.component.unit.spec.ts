import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of()),
    create: jest.fn().mockReturnValue(of()),
    update: jest.fn().mockReturnValue(of())
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule.withRoutes([
          { path: '**', component: FormComponent }
        ]),
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    
    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('form should be invalid when fields are missing or incorrect', () => {
    // name is missing
    component.sessionForm?.controls['name'].setValue(null);
    component.sessionForm?.controls['date'].setValue(new Date());
    component.sessionForm?.controls['teacher_id'].setValue(1);
    component.sessionForm?.controls['description'].setValue('azerty');
    expect(component.sessionForm?.valid).toBeFalsy();

    // date is missing
    component.sessionForm?.controls['name'].setValue('name');
    component.sessionForm?.controls['date'].setValue(null);
    component.sessionForm?.controls['teacher_id'].setValue(1);
    component.sessionForm?.controls['description'].setValue('azerty');
    expect(component.sessionForm?.valid).toBeFalsy();

    // teacher_id is missing
    component.sessionForm?.controls['name'].setValue('name');
    component.sessionForm?.controls['date'].setValue(new Date());
    component.sessionForm?.controls['teacher_id'].setValue(null);
    component.sessionForm?.controls['description'].setValue('azerty');
    expect(component.sessionForm?.valid).toBeFalsy();

    // description is missing
    component.sessionForm?.controls['name'].setValue('name');
    component.sessionForm?.controls['date'].setValue(new Date());
    component.sessionForm?.controls['teacher_id'].setValue(1);
    component.sessionForm?.controls['description'].setValue(null);
    expect(component.sessionForm?.valid).toBeFalsy();
  });

  it('form should be valid when fields are present', () => {
    component.sessionForm?.controls['name'].setValue('name');
    component.sessionForm?.controls['date'].setValue(new Date());
    component.sessionForm?.controls['teacher_id'].setValue(1);
    component.sessionForm?.controls['description'].setValue('azerty');
    expect(component.sessionForm?.valid).toBeTruthy();
  });

  it(`should call 'router.navigate()' when session is not admin`, () => {
    mockSessionService.sessionInformation.admin = false;
    let spyRouter = jest.spyOn(router, 'navigate');
    component.ngOnInit();
    expect(spyRouter).toHaveBeenCalledWith(['/sessions']);
    mockSessionService.sessionInformation.admin = true;
  });

  it(`should call 'sessionApiService.detail()' when route contains 'update'`, () => {
    expect(component.onUpdate).toBeFalsy();
    jest.spyOn(router, 'url', 'get').mockReturnValueOnce('/update/1');
    component.ngOnInit();
    expect(component.onUpdate).toBeTruthy();
    expect(mockSessionApiService.detail).toHaveBeenCalled();
  });

  it(`should call 'sessionApiService.create()' when 'onUpdate' is false`, () => {
    component.submit();
    expect(mockSessionApiService.create).toHaveBeenCalled();
  });

  it(`should call 'sessionApiService.update()' when 'onUpdate' is true`, () => {
    component.onUpdate = true;
    component.submit();
    expect(mockSessionApiService.update).toHaveBeenCalled();
  });
});
