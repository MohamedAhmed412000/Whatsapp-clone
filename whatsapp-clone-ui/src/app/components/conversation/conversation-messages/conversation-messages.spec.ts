import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConversationMessages } from './conversation-messages';

describe('ConversationMessages', () => {
  let component: ConversationMessages;
  let fixture: ComponentFixture<ConversationMessages>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConversationMessages]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConversationMessages);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
