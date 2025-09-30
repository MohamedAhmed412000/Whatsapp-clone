import { ComponentFixture, TestBed } from '@angular/core/testing';

import {ConversationSendMessage} from './conversation-send-message';

describe('ConversationSendMessage', () => {
  let component: ConversationSendMessage;
  let fixture: ComponentFixture<ConversationSendMessage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConversationSendMessage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConversationSendMessage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
