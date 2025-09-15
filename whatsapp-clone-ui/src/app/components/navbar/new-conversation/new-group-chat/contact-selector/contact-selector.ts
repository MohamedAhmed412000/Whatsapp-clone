import {Component, EventEmitter, input, Output} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {environment} from '../../../../../../environments/environment';

@Component({
  selector: 'app-contact-selector',
  imports: [
    FaIconComponent
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
  protected mediaBaseUrl: string = environment.MEDIA_URL;

  @Output() selectionChange = new EventEmitter<{ checked: boolean }>();

  toggleUser() : void {
    this.user.checked = !this.user.checked;
    this.selectionChange.emit({
      checked: this.user.checked
    });
  }

}
