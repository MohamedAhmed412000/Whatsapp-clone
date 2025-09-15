import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewContactModal } from './new-contact-modal';

describe('NewContactModal', () => {
  let component: NewContactModal;
  let fixture: ComponentFixture<NewContactModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewContactModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewContactModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
