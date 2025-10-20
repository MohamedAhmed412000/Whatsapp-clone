import {Component, EventEmitter, input, Output} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {MediaUrlPipe} from '../../../../utils/media/media-url.pipe';
import {UserResponse} from '../../../../services/user/models/user-response';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-conversation-selector',
  imports: [
    FaIconComponent,
    MediaUrlPipe,
    AsyncPipe
  ],
  templateUrl: './conversation-selector.html',
  styleUrl: './conversation-selector.scss'
})
export class ConversationSelector {
  user = input.required<UserResponse>();
  subtitle = input.required<string>();
  pictureReference = input.required<string>();

  @Output() userChatSelected = new EventEmitter<void>();

  onClick() : void {
    this.userChatSelected.emit();
  }

  get fullName() {
    return `${this.user().firstName} ${this.user().lastName}`;
  }
}
