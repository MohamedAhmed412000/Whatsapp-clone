import {Component, input, InputSignal} from '@angular/core';
import {ChatResponse} from '../../services/models/chat-response';
import {DatePipe} from '@angular/common';
import {UserResponse} from '../../services/models/user-response';
import {UserControllerService} from '../../services/services/user-controller.service';

@Component({
  selector: 'app-chat-list',
  imports: [
    DatePipe
  ],
  templateUrl: './chat-list.html',
  styleUrl: './chat-list.scss'
})
export class ChatList {
  chats: InputSignal<ChatResponse[]> = input<ChatResponse[]>([]);
  searchNewContact = false;
  contacts: Array<UserResponse> = [];

  constructor(
    private userService: UserControllerService
  ) {

  }

  searchContact() {
    this.userService.getUsers()
    .subscribe({
      next: (users) => {
        this.contacts = Array.isArray(users) ? users : [];
        console.log(users);
        console.log(Array.isArray(users));
        this.searchNewContact = true;
      },
      error: (error) => {
        console.error('Failed to fetch users:', error);
      }
    })
  }

  chatClicked(chat: ChatResponse) {

  }

  wrapMessage(lastMessage: String | undefined) {
    if (lastMessage && lastMessage.length <= 20) {
      return lastMessage;
    }
    return lastMessage?.substring(0, 17) + '...';
  }

  selectContact(contact: UserResponse) {

  }
}
