import {Component, effect, EventEmitter, input, Output} from '@angular/core';
import {UserResponse} from '../../services/user/models/user-response';
import {KeycloakService} from '../../utils/keycloak/keycloak.service';
import {ChatsControllerService} from '../../services/core/services';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {ConversationCompactInfo} from './conversation-compact-info/conversation-compact-info';
import {ChatResponse} from '../../services/core/models/chat-response';
import {MessageResponse} from '../../services/core/models/message-response';
import {ChatTrigger} from '../conversation/conversation-messages/chat-trigger';

@Component({
  selector: 'app-conversations',
  imports: [
    FaIconComponent,
    ConversationCompactInfo
  ],
  templateUrl: './conversations.html',
  styleUrl: './conversations.scss'
})
export class Conversations {
  @Output() chatSelected = new EventEmitter<ChatResponse>();
  messageCreated = input<ChatTrigger | null>();

  selectedChatId: string | undefined;
  conversations: Array<ChatResponse> | undefined;
  contact: UserResponse | undefined;

  constructor(
    private chatService: ChatsControllerService,
    private keycloakService: KeycloakService,
  ) {
    this.contact = this.keycloakService.me;
    this.chatService.getChatsByUser().subscribe(res => {
      this.conversations = res.body;
    });
    effect(() => {
      if (this.messageCreated()) this.onMessageCreated(this.messageCreated()!);
    });
  }

  selectChat(conversation: ChatResponse) {
    this.selectedChatId = conversation.id;
    this.chatService.getChatDetails({
      'chat-id': conversation.id as string,
    }).subscribe(res => {
      res.body!.unreadCount = 0;
      this.chatSelected.emit(res.body!);
      this.conversations = this.conversations?.map(chat =>
        chat.id === this.selectedChatId ? res.body! : chat
      );
    })
  }

  updateSelectedChat(message: MessageResponse) {
    this.conversations?.filter(c => c.id === this.selectedChatId)
      ?.forEach(chat => {
        chat.lastMessage = message.type === 'TEXT'? message.content: 'Attachment';
        chat.lastMessageTime = message.createdAt;
      })
  }

  onMessageCreated(chatTrigger: ChatTrigger) {
    const chatIndex = this.conversations?.findIndex(c => c.id === chatTrigger.chat.id)!;
    if (chatIndex >= 0) this.conversations?.splice(chatIndex, 1);
    this.conversations?.unshift(chatTrigger.chat);
    this.conversations = [...this.conversations!];
  }
}
