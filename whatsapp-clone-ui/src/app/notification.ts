export interface Notification {
  id?: string;
  chatId?: string;
  content?: string;
  senderId?: string;
  receiversId?: string[];
  messageType?: 'TEXT' | 'MEDIA' | 'AUDIO';
  notificationType?: 'SEEN' | 'MESSAGE' | 'MEDIA' | 'AUDIO' | 'USER_CONNECTED' | 'USER_DISCONNECTED';
  chatName?: string;
  mediaReferencesList?: string[];
}
