import {Component, effect, input} from '@angular/core';
import {UserResponse} from '../../../services/user/models/user-response';
import {KeycloakService} from '../../../utils/keycloak/keycloak.service';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {environment} from '../../../../environments/environment';
import {ChatResponse} from '../../../services/core/models/chat-response';
import {RelativeChatDatePipe} from '../../../utils/relative-chat-date.pipe';
import {MediaUrlPipe} from '../../../utils/media/media-url.pipe';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-conversation-compact-info',
  imports: [
    FaIconComponent,
    RelativeChatDatePipe,
    MediaUrlPipe,
    AsyncPipe,
  ],
  templateUrl: './conversation-compact-info.html',
  styleUrl: './conversation-compact-info.scss'
})
export class ConversationCompactInfo {
  conversation = input.required<ChatResponse>();
  isSelected = input.required<boolean>();

  protected contact: UserResponse | undefined;

  constructor(
    private keycloakService: KeycloakService,
  ) {
    effect(() => {
        this.contact = this.keycloakService.me;
    });
  }

  isSelfChat(): boolean {
    return this.conversation()?.receiversId!.every(userId => userId === this.keycloakService.userId);
  }
}
