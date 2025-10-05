import {Component, EventEmitter, input, Output} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {MediaUrlPipe} from '../../../../../utils/media/media-url.pipe';

@Component({
  selector: 'app-contact-selector',
  imports: [
    FaIconComponent,
    MediaUrlPipe
  ],
  templateUrl: './contact-selector.html',
  styleUrl: './contact-selector.scss'
})
export class ContactSelector {
  title = input.required<string>();
  subtitle = input.required<string>();
  pictureReference = input.required<string>();
  protected user = {
    checked: false,
  };

  @Output() selectionChange = new EventEmitter<{ checked: boolean }>();

  toggleUser() : void {
    this.user.checked = !this.user.checked;
    this.selectionChange.emit({
      checked: this.user.checked
    });
  }

}
