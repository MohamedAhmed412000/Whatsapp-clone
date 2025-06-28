import {Component, EventEmitter, input, Output} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-conversation-selector',
  imports: [
    FaIconComponent
  ],
  templateUrl: './conversation-selector.html',
  styleUrl: './conversation-selector.scss'
})
export class ConversationSelector {
  title = input.required<string>();
  subtitle = input.required<string>();
  pictureUrl = input.required<string>();

  @Output() select = new EventEmitter<void>();

  onClick() : void {
    this.select.next();
  }

}
