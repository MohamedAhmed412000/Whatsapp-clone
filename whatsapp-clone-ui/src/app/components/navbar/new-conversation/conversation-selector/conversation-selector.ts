import {Component, EventEmitter, input, Output} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {MediaUrlPipe} from '../../../../utils/media-url.pipe';

@Component({
  selector: 'app-conversation-selector',
  imports: [
    FaIconComponent,
    MediaUrlPipe
  ],
  templateUrl: './conversation-selector.html',
  styleUrl: './conversation-selector.scss'
})
export class ConversationSelector {
  title = input.required<string>();
  subtitle = input.required<string>();
  pictureReference = input.required<string>();

  @Output() select = new EventEmitter<void>();

  onClick() : void {
    this.select.next();
  }

}
