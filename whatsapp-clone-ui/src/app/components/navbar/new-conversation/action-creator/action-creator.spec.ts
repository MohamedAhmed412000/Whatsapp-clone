import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActionCreator } from './action-creator';

describe('ActionCreator', () => {
  let component: ActionCreator;
  let fixture: ComponentFixture<ActionCreator>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActionCreator]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActionCreator);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
