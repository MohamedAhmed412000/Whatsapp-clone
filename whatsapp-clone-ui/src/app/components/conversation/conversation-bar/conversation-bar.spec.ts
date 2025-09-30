import { ComponentFixture, TestBed } from '@angular/core/testing';

import {ConversationBar} from './conversation-bar';

describe('conversation-bar', () => {
  let component: ConversationBar;
  let fixture: ComponentFixture<ConversationBar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConversationBar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConversationBar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
