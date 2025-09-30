import {
  AfterViewChecked,
  AfterViewInit,
  Component,
  computed,
  ElementRef, EventEmitter,
  input, Output,
  signal,
  ViewChild
} from '@angular/core';
import {DatePipe, NgStyle} from '@angular/common';
import {KeycloakService} from '../../../utils/keycloak/keycloak.service';
import {ChatMessageResponse} from '../../../services/core/models/chat-message-response';
import {MessageResponse} from '../../../services/core/models/message-response';
import {ChatResponse} from '../../../services/core/models/chat-response';
import {ChatUserResponse} from '../../../services/core/models/chat-user-response';
import {RelativeChatDatePipe} from '../../../utils/relative-messages-date.pipe';
import {MediaUrlPipe} from '../../../utils/media-url.pipe';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-conversation-messages',
  imports: [
    DatePipe,
    RelativeChatDatePipe,
    MediaUrlPipe,
    FaIconComponent,
    NgStyle
  ],
  templateUrl: './conversation-messages.html',
  styleUrl: './conversation-messages.scss'
})
export class ConversationMessages implements AfterViewInit, AfterViewChecked {
  @Output() repliedMessageEvent = new EventEmitter<MessageResponse>();
  chat = input<ChatResponse>();

  chatMessages = input<ChatMessageResponse>();
  chatUsers = input<ChatUserResponse[]>();
  chatUsersMap = computed(() => {
    const users = this.chatUsers();
    return new Map(users!.map(user => [user.id, user]));
  });
  showContextMenu = signal(false);
  contextMenuPosition = signal<{ x: string; y: string }>({ x: '0px', y: '0px' });
  @ViewChild('scrollableDiv') private scrollableDiv!: ElementRef;

  private shouldScroll = true;
  private repliedMessage: MessageResponse | undefined;

  constructor(
    private keycloakService: KeycloakService,
  ) {
    document.addEventListener('click', () => this.closeContextMenu());
  }

  ngAfterViewInit(): void {
    this.scrollToBottom();
  }

  ngAfterViewChecked(): void {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  onRightClick(event: MouseEvent, message: MessageResponse) {
    event.preventDefault();
    this.showContextMenu.set(true);
    this.contextMenuPosition.set({
      x: `${event.clientX}px`,
      y: `${event.clientY}px`
    });
    this.repliedMessage = message;
  }

  closeContextMenu() {
    this.showContextMenu.set(false);
  }

  private scrollToBottom(): void {
    if (!this.scrollableDiv) return;
    try {
      const el = this.scrollableDiv.nativeElement;
      el.scrollTop = 0;
    } catch (err) {
      console.error('Scroll error:', err);
    }
  }

  getSortedDates() {
    const map = this.chatMessages();
    if (!map) return [];
    this.shouldScroll = true;
    return Object.keys(map!);
  }

  getSortedMessagesForDate(date: string): MessageResponse[] {
    const map = this.chatMessages();
    return (map?.[date] ?? []);
  }

  getChatUserFullName(userId: string): string {
    return this.chatUsersMap().get(userId)?.fullname!;
  }

  isSelfMessage(message: MessageResponse) {
    return message.senderId === this.keycloakService.userId;
  }

  replyMessage() {
    this.repliedMessageEvent.emit(this.repliedMessage);
  }
}
