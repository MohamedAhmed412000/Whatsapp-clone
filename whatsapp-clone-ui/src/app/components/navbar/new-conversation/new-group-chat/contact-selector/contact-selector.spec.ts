import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContactSelector } from './contact-selector';

describe('ContactSelector', () => {
  let component: ContactSelector;
  let fixture: ComponentFixture<ContactSelector>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContactSelector]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContactSelector);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
