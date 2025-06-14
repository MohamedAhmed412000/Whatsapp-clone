import {Component, OnInit} from '@angular/core';
import {ChatList} from '../../components/chat-list/chat-list';
import {ChatResponse} from '../../services/models/chat-response';
import {ChatControllerService} from '../../services/services/chat-controller.service';
import {KeycloakService} from '../../utils/keycloak/keycloak.service';

@Component({
  selector: 'app-main',
  imports: [
    ChatList
  ],
  templateUrl: './main.html',
  styleUrl: './main.scss'
})
export class Main implements OnInit {

  chats: Array<ChatResponse> = [];
  constructor(
    private chatService: ChatControllerService,
    private keycloakService: KeycloakService
  ) {}

  ngOnInit(): void {
    this.getAllChats();
  }

  private getAllChats() {
    this.chatService.getChatsByUser()
    .subscribe(res => {
      this.chats = res;
    })
  }

  logout() {
    this.keycloakService.logout();
  }

  userProfile() {
    this.keycloakService.accountManagement();
  }
}
