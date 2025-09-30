import {ChatResponse} from '../../../services/core/models/chat-response';

export interface ChatTrigger {
  chat: ChatResponse;
  messageId: number;
}
