import {Component, input} from '@angular/core';
import {ChatResponse} from '../../services/core/models/chat-response';

@Component({
  selector: 'app-chat',
  imports: [],
  templateUrl: './chat.html',
  styleUrl: './chat.scss'
})
export class Chat {
  chat = input.required<ChatResponse>();

}
