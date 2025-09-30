import {Component, OnInit, signal, ViewChild} from '@angular/core';
import {FaIconLibrary} from '@fortawesome/angular-fontawesome';
import {fontAwesomeIcons} from './shared/font-awesome-icons';
import {Navbar} from './components/navbar/navbar';
import {Toast} from './shared/toast/toast';
import {ToastInfo} from './shared/toast/toast-info.model';
import {NgbToast} from '@ng-bootstrap/ng-bootstrap';
import {Conversations} from './components/conversations/conversations';
import {KeycloakService} from './utils/keycloak/keycloak.service';
import {ConversationBar} from './components/conversation/conversation-bar/conversation-bar';
import {ConversationMessages} from './components/conversation/conversation-messages/conversation-messages';
import {ConversationSendMessage} from './components/conversation/conversation-send-message/conversation-send-message';
import {ChatResponse} from './services/core/models/chat-response';
import {MessageResponse} from './services/core/models/message-response';
import {ChatsControllerService} from './services/core/services/chats-controller.service';
import {ChatUsersControllerService} from './services/core/services/chat-users-controller.service';
import {MessagesControllerService} from './services/core/services/messages-controller.service';
import {ChatMessageResponse} from './services/core/models/chat-message-response';
import {ChatUserResponse} from './services/core/models/chat-user-response';
import {ChatTrigger} from './components/conversation/conversation-messages/ChatTrigger';

@Component({
  selector: 'app-root',
  imports: [Navbar, NgbToast, Conversations, ConversationBar, ConversationMessages, ConversationSendMessage],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'whatsapp-clone-ui';
  isChatSelected = false;
  repliedMessage: MessageResponse | undefined;
  selectedChat: ChatResponse | undefined;
  chatUsersString: string | undefined;
  chatUsers: ChatUserResponse[] | undefined;
  chatMessages: ChatMessageResponse | undefined;

  messageCreated = signal<ChatTrigger | null>(null)
  @ViewChild('conversationsList') conversationsList!: Conversations;

  constructor(
    private toastService: Toast,
    private faIconLibrary: FaIconLibrary,
    private keycloakService: KeycloakService,
    private chatUserService: ChatUsersControllerService,
    private messageService: MessagesControllerService
  ) {}

  ngOnInit() {
    this.initFontAwesome();
    this.isChatSelected = false;
  }

  onChatSelected(chat: ChatResponse) {
    this.isChatSelected = true;
    this.selectedChat = chat;
    this.messageService.getChatMessages({
      'chatId': chat.id as string,
      'page': 0
    }).subscribe(res => {
      this.chatMessages = res.body;
    });
    this.chatUserService.getChatUsers({
      'chat-id': chat.id as string,
    }).subscribe(res => {
      if (chat.isGroupChat) {
        this.chatUsersString = res.body
          ?.filter(chatUser => chatUser.id != this.keycloakService.userId)
          ?.map(chatUser => chatUser.fullname)
          ?.join(', ');
        this.chatUsers = res.body!;
      } else {
        this.chatUsersString = undefined;
        this.chatUsers = undefined;
      }
    })
  }

  onMessageCreated(message: MessageResponse) {
    this.conversationsList.updateSelectedChat(message);
    const today = new Date().toISOString().split("T")[0];
    if (!this.chatMessages![today]) {
      this.chatMessages = {
        [today]: [],
        ...this.chatMessages
      };
    }
    this.chatMessages![today].unshift(message);
    this.messageCreated.set({
      chat: this.selectedChat!,
      messageId: message.id!
    });
  }

  allToasts(): ToastInfo[] {
    return this.toastService.toasts;
  }

  removeToast(toast: ToastInfo) {
    this.toastService.remove(toast);
  }

  private initFontAwesome() {
    this.faIconLibrary.addIcons(...fontAwesomeIcons);
  }

  onMessageReplied(message: MessageResponse) {
    this.repliedMessage = message;
  }
}
