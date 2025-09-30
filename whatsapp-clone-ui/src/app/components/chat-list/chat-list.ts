import {Component, output} from '@angular/core';
import {DatePipe} from '@angular/common';
import {KeycloakService} from '../../utils/keycloak/keycloak.service';
import {UserResponse} from '../../services/user/models/user-response';
import {UsersControllerService} from '../../services/user/services/users-controller.service';
import {ChatsControllerService} from '../../services/core/services/chats-controller.service';
import {MessageResponse} from '../../services/core/models/message-response';
import {ChatResponse} from '../../services/core/models/chat-response';
import {MessagesControllerService} from '../../services/core/services/messages-controller.service';

@Component({
  selector: 'app-chat-list',
  imports: [
    DatePipe
  ],
  templateUrl: './chat-list.html',
  styleUrl: './chat-list.scss'
})
export class ChatList {
  chats = Array<ChatResponse>();
  searchNewContact = false;
  contacts: Array<UserResponse> = [];
  chatMessages: MessageResponse[] = [];
  selectedChat: ChatResponse = {};

  constructor(
    private userService: UsersControllerService,
    private chatService: ChatsControllerService,
    private keycloakService: KeycloakService,
    private messageService: MessagesControllerService
  ) {
    this.getAllChats();
  }

  getAllChats() {
    // this.chatService.getChatsByUser()
    //   .subscribe(res => {
    //     // this.chats = res.body ?? [];
    //   })
  }

  chatSelected(chatResponse: ChatResponse) {
    this.selectedChat = chatResponse;
    // this.getAllChatMessages(chatResponse.id as string);
    // this.setMessagesToSeen();
    this.selectedChat.unreadCount = 0;
  }

  private getAllChatMessages(chatId: string) {
    // this.messageService.getChatMessages({
    //   chatId: chatId,
    //   page: 0
    // }).subscribe({
    //   next: (res) => {
    //     // this.chatMessages = res.body ?? [];
    //   }
    // });
  }

  searchContact() {
    this.userService.getUsers()
    .subscribe({
      next: (users) => {
        this.contacts = Array.isArray(users) ? users : [];
        this.searchNewContact = true;
      },
      error: (error) => {
        console.error('Failed to fetch users:', error);
      }
    })
  }

  chatClicked(chat: ChatResponse) {
    // this.chatSelected.emit(chat);
  }

  wrapMessage(lastMessage: String | undefined) {
    if (lastMessage && lastMessage.length <= 20) {
      return lastMessage;
    }
    return lastMessage?.substring(0, 17) + '...';
  }

  selectContact(contact: UserResponse) {
    // this.chatService.createChat({
    //   'receiver-id': contact.id as string
    // }).subscribe({
    //   next: (res) => {
    //     const chat: ChatResponse = {
    //       // id: res.body?.content,
    //       // name: contact.firstName + ' ' + contact.lastName,
    //       // senderId: this.keycloakService.userId as string,
    //       // receiversId: [contact.id as string],
    //       // isRecipientOnline: contact.online,
    //       // unreadCount: 0,
    //       // lastMessage: 'Chat created',
    //       // lastMessageTime: new Date().toISOString()
    //     };
    //     this.chats.unshift(chat);
    //     this.searchNewContact = false;
    //     // this.chatSelected.emit(chat);
    //   }
    // });
  }
}
