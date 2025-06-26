export interface Notification {
  chatId?: string;
  content?: string;
  senderId?: string;
  receiversId?: string[];
  messageType?: 'TEXT' | 'MEDIA' | 'AUDIO';
  notificationType?: 'SEEN' | 'MESSAGE' | 'MEDIA' | 'AUDIO';
  chatName?: string;
  media?: Array<String>;
}
