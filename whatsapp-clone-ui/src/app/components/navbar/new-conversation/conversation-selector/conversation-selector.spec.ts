import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConversationSelector } from './conversation-selector';

describe('ConversationSelector', () => {
  let component: ConversationSelector;
  let fixture: ComponentFixture<ConversationSelector>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConversationSelector]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConversationSelector);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
