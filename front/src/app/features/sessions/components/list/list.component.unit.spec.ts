import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { getByTestId, queryByTestId } from '@testing-library/angular';
import '@testing-library/jest-dom';

import { SessionService } from 'src/app/services/session.service';
import { ListComponent } from './list.component';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [{ provide: SessionService, useValue: mockSessionService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(`should display 'create' button when user is admin`, () => {
    expect(getByTestId(document.body, 'admin-create-btn')).toBeInTheDocument();
    expect(getByTestId(document.body, 'admin-create-btn')).toHaveTextContent('Create');
  });

  it(`should not display 'create' button when user is not admin`, () => {
    mockSessionService.sessionInformation.admin = false;
    fixture.detectChanges();
    expect(queryByTestId(document.body, 'admin-create-btn')).toBeNull();
  });
});
