import {Component, input} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {ChatResponse} from '../../../services/core/models/chat-response';
import {environment} from '../../../../environments/environment';
import {MediaUrlPipe} from '../../../utils/media/media-url.pipe';
import {KeycloakService} from '../../../utils/keycloak/keycloak.service';

@Component({
  selector: 'app-conversation-bar',
  imports: [
    FaIconComponent,
    MediaUrlPipe
  ],
  templateUrl: './conversation-bar.html',
  styleUrl: './conversation-bar.scss'
})
export class ConversationBar {
  chat = input.required<ChatResponse>();
  chatUsers = input.required<string>();

  constructor(
    private keycloakService: KeycloakService
  ) {}

  isSelfChat(): boolean {
    return this.chat()?.receiversId!.every(userId => userId === this.keycloakService.userId);
  }
}
