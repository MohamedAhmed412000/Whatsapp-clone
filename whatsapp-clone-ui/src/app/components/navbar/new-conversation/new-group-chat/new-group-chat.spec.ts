import { ComponentFixture, TestBed } from '@angular/core/testing';

import {NewGroupChat} from './new-group-chat';

describe('NewGroupChat', () => {
  let component: NewGroupChat;
  let fixture: ComponentFixture<NewGroupChat>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewGroupChat]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewGroupChat);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
