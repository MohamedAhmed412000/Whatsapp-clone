import {Component, effect, EventEmitter, inject, input, Output} from '@angular/core';
import {UserResponse} from '../../services/user/models/user-response';
import {KeycloakService} from '../../utils/keycloak/keycloak.service';
import {MessagesControllerService} from '../../services/core/services';

@Component({
  selector: 'app-conversations',
  imports: [],
  templateUrl: './conversations.html',
  styleUrl: './conversations.scss'
})
export class Conversations {

  conversation = input.required();
  connectedUser = input.required<UserResponse>();

  messageService = inject(MessagesControllerService);
  keycloakService = inject(KeycloakService);

  @Output() select = new EventEmitter();
  @Output() delete = new EventEmitter();

  protected showMenu = false;
  nbOfUnReadMessage = 0;
  contact: UserResponse | undefined;

  showConversation() {
    this.select.emit(this.conversation());
  }

  constructor() {
    effect(() => {
      this.keycloakService.me.subscribe(user => {
        this.contact = user;
      });
    });
  }

}
