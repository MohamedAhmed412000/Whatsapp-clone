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
import {ChatUsersControllerService} from './services/core/services/chat-users-controller.service';
import {MessagesControllerService} from './services/core/services/messages-controller.service';
import {ChatMessageResponse} from './services/core/models/chat-message-response';
import {ChatUserResponse} from './services/core/models/chat-user-response';
import {ChatTrigger} from './components/conversation/conversation-messages/chat-trigger';
import {UserResponse} from './services/user/models/user-response';
import {ChatsControllerService} from './services/core/services/chats-controller.service';
import SockJS from 'sockjs-client';
import {Message, Stomp} from '@stomp/stompjs';
import {Notification} from './pages/main/notification';
import {environment} from '../environments/environment.development';

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
  socketClient: any = null;
  notificationSubscription: any;

  messageCreated = signal<ChatTrigger | null>(null)
  @ViewChild('conversationsList') conversationsList!: Conversations;

  constructor(
    private toastService: Toast,
    private faIconLibrary: FaIconLibrary,
    private keycloakService: KeycloakService,
    private chatUserService: ChatUsersControllerService,
    private messageService: MessagesControllerService,
    private chatService: ChatsControllerService
  ) {
    this.keycloakService.getMe();
  }

  ngOnInit() {
    this.initFontAwesome();
    this.initWebSocket();
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
      this.chatUsers = res.body!;
      if (chat.isGroupChat) {
        this.chatUsersString = res.body
          ?.filter(chatUser => chatUser.id != this.keycloakService.userId)
          ?.map(chatUser => chatUser.fullname)
          ?.join(', ');
      } else {
        this.chatUsersString = undefined;
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

  onUserChatSelected(user: UserResponse) {
    let conversation = this.conversationsList.conversations?.find(c =>
      c.isGroupChat === false && c.receiversId?.includes(user.id!)
    );
    if (conversation) {
      this.selectedChat = conversation;
      this.conversationsList.selectedChatId = conversation.id;
      this.onChatSelected(conversation);
    } else {
      this.chatService.createChat({
        'receiver-id': user.id as string
      }).subscribe({
        next: res => {
          const newConversation: ChatResponse = {
            id: res?.body?.content,
            name: `${user.firstName} ${user.lastName}`,
            senderId: this.keycloakService.userId,
            description: '',
            isGroupChat: false,
            receiversId: [user.id as string],
            unreadCount: 0,
            chatImageReference: user.profilePictureReference,
            isRecipientOnline: user.online,
            lastMessage: "Chat created",
            lastMessageTime: new Date().toISOString()
          };
          this.conversationsList.conversations?.unshift(newConversation);
          this.selectedChat = newConversation;
          this.conversationsList.selectedChatId = newConversation.id;
          this.onChatSelected(newConversation);
        },
        error: err => console.error(err)
      });
    }
  }

  onChatUserClicked(chatUser: ChatUserResponse) {
    let conversation = this.conversationsList.conversations?.find(c =>
      c.isGroupChat === false && c.receiversId?.includes(chatUser.id!)
    );
    if (conversation) {
      this.selectedChat = conversation;
      this.conversationsList.selectedChatId = conversation.id;
      this.onChatSelected(conversation);
    } else {
      this.chatService.createChat({
        'receiver-id': chatUser.id as string
      }).subscribe({
        next: res => {
          const newConversation: ChatResponse = {
            id: res?.body?.content,
            name: chatUser.fullname,
            senderId: this.keycloakService.userId,
            description: '',
            isGroupChat: false,
            receiversId: [chatUser.id as string],
            unreadCount: 0,
            chatImageReference: chatUser.imageFileReference,
            isRecipientOnline: chatUser.online,
            lastMessage: "Chat created",
            lastMessageTime: new Date().toISOString()
          };
          this.conversationsList.conversations?.unshift(newConversation);
          this.selectedChat = newConversation;
          this.conversationsList.selectedChatId = newConversation.id;
          this.onChatSelected(newConversation);
        },
        error: err => console.error(err)
      });
    }
  }

  private initWebSocket() {
    if (this.keycloakService.keycloak.tokenParsed?.sub) {
      let ws = new SockJS(environment.WEB_SOCKET_URL);
      this.socketClient = Stomp.over(() => ws);
      this.socketClient.reconnectDelay = 5000;
      this.socketClient.debug = () => false;
      const subUrl = `/topic/chat.${this.keycloakService.keycloak.tokenParsed?.sub}`;
      this.socketClient.connect({'Authorization': `Bearer ${this.keycloakService.keycloak.token}`},
        () => {
          console.log("Connected to WebSocket");
          this.notificationSubscription = this.socketClient.subscribe(subUrl, (message: Message) => {
            const notification: Notification = JSON.parse(message.body);
            this.handleNotification(notification);
          }, () => console.error('Error while connecting to WebSocket'));
        }
      );
    }
  }

  private handleNotification(notification: Notification) {
    if (!notification) return;
    if (this.selectedChat && this.selectedChat.id === notification.chatId) {
      switch (notification.notificationType) {
        case 'MESSAGE':
        case 'MEDIA':
          const message: MessageResponse = {
            id: Number(notification.id),
            senderId: notification.senderId,
            content: notification.content,
            type: notification.messageType,
            mediaListReferences: notification.mediaReferencesList,
            createdAt: new Date().toISOString()
          };
          if (notification.notificationType === 'MEDIA') {
            this.selectedChat.lastMessage = 'Attachment';
          } else {
            this.selectedChat.lastMessage = message.content;
          }
          this.onMessageCreated(message);
          break;
        case 'SEEN':
          if (!this.selectedChat.isGroupChat) {
            Object.keys(this.chatMessages!).forEach(date => {
              this.chatMessages![date].forEach(message => {
                message.state = 'SEEN';
              })
            })
          }
          break;
      }
    } else {
      const chatIndex = this.conversationsList.conversations!.findIndex(chat =>
        chat.id === notification.chatId);
      const destChat = chatIndex !== -1 ? this.conversationsList.conversations!
        .splice(chatIndex, 1)[0] : undefined;
      if (destChat && notification.notificationType !== 'SEEN') {
        if (notification.notificationType === 'MESSAGE') {
          destChat.lastMessage = notification.content;
        } else if (notification.notificationType === 'MEDIA') {
          destChat.lastMessage = 'Attachment';
        }
        destChat.lastMessageTime = new Date().toISOString();
        destChat.unreadCount!++;
        this.conversationsList.conversations!.unshift(destChat);
      } else if (notification.notificationType === 'MESSAGE') {
        const newChat: ChatResponse = {
          id: notification.chatId,
          senderId: notification.senderId,
          receiversId: notification.receiversId,
          name: notification.chatName,
          lastMessage: notification.content,
          lastMessageTime: new Date().toISOString(),
          unreadCount: 1
        }
        this.conversationsList.conversations!.unshift(newChat);
      }
    }
  }
}
