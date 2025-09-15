import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupChatCreator } from './group-chat-creator';

describe('GroupChatCreator', () => {
  let component: GroupChatCreator;
  let fixture: ComponentFixture<GroupChatCreator>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GroupChatCreator]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GroupChatCreator);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
