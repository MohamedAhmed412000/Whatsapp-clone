import {Component, OnDestroy, OnInit} from '@angular/core';
import {ChatResponse} from '../../services/core/models/chat-response';
import {KeycloakService} from '../../utils/keycloak/keycloak.service';
import {DatePipe} from '@angular/common';
import {PickerComponent} from '@ctrl/ngx-emoji-mart';
import {FormsModule} from '@angular/forms';
import {EmojiData} from '@ctrl/ngx-emoji-mart/ngx-emoji';
import SockJS from 'sockjs-client';
import { Client, Message } from '@stomp/stompjs';
import {Notification} from './notification';
import {ChatsControllerService} from '../../services/core/services/chats-controller.service';
import {MessageResponse} from '../../services/core/models/message-response';
import {MessagesControllerService} from '../../services/core/services/messages-controller.service';
import {MessageCreationResource} from '../../services/core/models/message-creation-resource';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-main',
  imports: [
    DatePipe,
    PickerComponent,
    FormsModule
  ],
  templateUrl: './main.html',
  styleUrl: './main.scss'
})
export class Main implements OnInit, OnDestroy {

  chats: ChatResponse[] = [];
  selectedChat: ChatResponse = {};
  chatMessages: MessageResponse[] = [];
  mediaBaseUrl: string = environment.MEDIA_URL;
  showEmojis: any = false;
  messageContent: any = '';
  socketClient: any = null;
  private notificationSubscription: any;

  constructor(
    private chatService: ChatsControllerService,
    private keycloakService: KeycloakService,
    private messageService: MessagesControllerService
  ) {}

  ngOnDestroy(): void {
    // if (this.socketClient !== null) {
    //   this.socketClient.disconnect();
    //   this.notificationSubscription.unsubscribe();
    //   this.socketClient = null;
    // }
  }

  ngOnInit(): void {
    // this.initWebSocket();
    this.getAllChats();
  }

  private getAllChats() {
    this.chatService.getChatsByUser()
      .subscribe(res => {
        this.chats = res.body ?? [];
      })
  }

  logout() {
    this.keycloakService.logout();
  }

  userProfile() {
    this.keycloakService.accountManagement();
  }

  chatSelected(chatResponse: ChatResponse) {
    this.selectedChat = chatResponse;
    this.getAllChatMessages(chatResponse.id as string);
    // this.setMessagesToSeen();
    this.selectedChat.unreadCount = 0;
  }

  private getAllChatMessages(chatId: string) {
    this.messageService.getChatMessages({
      chatId: chatId,
      page: 0
    }).subscribe({
      next: (res) => {
        // this.chatMessages = res.body ?? [];
      }
    });
  }

  private setMessagesToSeen() {
    // this.messageService.setMessagesToSeen({
    //   chatId: this.selectedChat.id as string,
    // }).subscribe({
    //   next: () => {}
    // });
  }

  isSelfMessage(message: MessageResponse) {
    return message.senderId === this.keycloakService.userId;
  }

  uploadMedia(target: EventTarget | null) {

  }

  onSelectEmojis(emojiSelected: any) {
    const emoji: EmojiData = emojiSelected.emoji;
    if (this.messageContent) {
      const firstChar = this.messageContent.charAt(0);
      if (/[\u0600-\u06FF]/.test(firstChar)) {
        this.messageContent = emoji.native + this.messageContent;
      } else {
        this.messageContent += emoji.native;
      }
    } else {
      this.messageContent = emoji.native;
    }
  }

  keyDown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.sendMessage();
    }
  }

  onClick() {
    this.setMessagesToSeen();
  }

  sendMessage() {
    if (this.messageContent) {
      const messageRequest: MessageCreationResource = {
        chatId: this.selectedChat.id as string,
        content: this.messageContent,
        messageType: "TEXT"
      };
      this.messageService.saveMessage({
        body: messageRequest
      }).subscribe({
        next: () => {
          const message: MessageResponse = {
            senderId: this.keycloakService.userId as string,
            content: this.messageContent,
            type: "TEXT",
            state: "SENT",
            createdAt: new Date().toISOString()
          };
          this.selectedChat.lastMessage = message.content;
          this.chatMessages.push(message);
          this.messageContent = '';
          this.showEmojis = false;
        }
      })
    }
  }

  private initWebSocket() {
    if (this.keycloakService.keycloak.tokenParsed?.sub) {
      const ws = new SockJS('http://localhost:8095/ws');
      this.socketClient = new Client({
        webSocketFactory: () => ws,
        connectHeaders: {
          Authorization: 'Bearer ' + this.keycloakService.keycloak.token,
        },
        debug: (str) => console.log(str),
        reconnectDelay: 5000, // try to reconnect after 5s if disconnected
      });
      const subUrl = `/users/${this.keycloakService.keycloak.tokenParsed?.sub}/chat`;
      this.socketClient.onConnect = () => {
        console.log('Connected to WebSocket');
        this.socketClient.subscribe(subUrl, (message: Message) => {
          console.log(message);
          const notification: Notification = JSON.parse(message.body);
          this.handleNotification(notification);
        });
      };

      this.socketClient.onStompError = (frame: { headers: { [x: string]: any; }; body: any; }) => {
        console.error('Broker reported error:', frame.headers['message']);
        console.error('Additional details:', frame.body);
      };

      this.socketClient.activate();
    }
  }

  private handleNotification(notification: Notification) {
    if (!notification) return;
    if (this.selectedChat && this.selectedChat.id === notification.chatId) {
      console.log('Selected Chat:', this.selectedChat);
      console.log(this.chatMessages.length);
      switch (notification.notificationType) {
        case 'MESSAGE':
        case 'MEDIA':
          const message: MessageResponse = {
            id: this.chatMessages.length + 1,
            senderId: notification.senderId,
            content: notification.content,
            type: notification.messageType,
            mediaListReferences: notification.mediaReferencesList,
            createdAt: new Date().toISOString()
          };
          console.log('Received Message:', message);
          if (notification.notificationType === 'MEDIA') {
            this.selectedChat.lastMessage = 'Attachment';
          } else {
            this.selectedChat.lastMessage = message.content;
          }
          this.chatMessages.push(message);
          break;
        case 'SEEN':
          this.chatMessages.forEach(message => message.state = 'SEEN');
          break;
      }
    } else {
      const chatIndex = this.chats.findIndex(chat => chat.id === notification.chatId);
      const destChat = chatIndex !== -1 ? this.chats.splice(chatIndex, 1)[0] : undefined;
      if (destChat && notification.notificationType !== 'SEEN') {
        if (notification.notificationType === 'MESSAGE') {
          destChat.lastMessage = notification.content;
        } else if (notification.notificationType === 'MEDIA') {
          destChat.lastMessage = 'Attachment';
        }
        destChat.lastMessageTime = new Date().toISOString();
        destChat.unreadCount!++;
        this.chats.unshift(destChat);
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
        this.chats.unshift(newChat);
      }
    }
  }
}
