import {Component, EventEmitter, input, Output} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-action-creator',
  imports: [
    FaIconComponent
  ],
  templateUrl: './action-creator.html',
  styleUrl: './action-creator.scss'
})
export class ActionCreator {
  title = input.required<string>();
  icon = input.required<string>();
  action = input.required<() => any>();

  onClick() : void {
    const fn = this.action();
    fn();
  }

}
