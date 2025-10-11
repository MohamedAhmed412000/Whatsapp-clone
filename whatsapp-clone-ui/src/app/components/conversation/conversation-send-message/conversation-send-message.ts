import {
  Component,
  ElementRef,
  OnChanges,
  EventEmitter,
  HostListener,
  input,
  model,
  Output,
  ViewChild,
  SimpleChanges
} from '@angular/core';
import {ChatResponse} from '../../../services/core/models/chat-response';
import {FormsModule} from '@angular/forms';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {PickerComponent} from '@ctrl/ngx-emoji-mart';
import {EmojiData} from '@ctrl/ngx-emoji-mart/ngx-emoji';
import {MessageCreationResource} from '../../../services/core/models/message-creation-resource';
import {MessageResponse} from '../../../services/core/models/message-response';
import {KeycloakService} from '../../../utils/keycloak/keycloak.service';
import {MessagesControllerService} from '../../../services/core/services/messages-controller.service';
import {NgbDropdown, NgbDropdownItem, NgbDropdownMenu, NgbDropdownToggle} from '@ng-bootstrap/ng-bootstrap';
import {ChatUserResponse} from '../../../services/core/models/chat-user-response';
import {RepliedMessage} from '../../../services/core/models/replied-message';
import {ColorizePipe} from '../../../utils/colorize.pipe';

@Component({
  selector: 'app-conversation-send-message',
  imports: [
    FormsModule,
    FaIconComponent,
    PickerComponent,
    NgbDropdown,
    NgbDropdownToggle,
    NgbDropdownItem,
    NgbDropdownMenu,
    ColorizePipe
  ],
  templateUrl: './conversation-send-message.html',
  styleUrl: './conversation-send-message.scss'
})
export class ConversationSendMessage implements OnChanges {
  @Output() messageCreated = new EventEmitter<MessageResponse>();
  selectedChat = input.required<ChatResponse>();
  chatUsers = input<ChatUserResponse[]>();
  repliedMessage = model<MessageResponse>();

  messageContent: any = '';
  showEmojis: any = false;

  @ViewChild('emojiPanel') emojiPanel!: ElementRef;
  @ViewChild('messageTextBox') messagesPanel!: ElementRef;

  constructor(
    private keycloakService: KeycloakService,
    private messageService: MessagesControllerService
  ) {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes['repliedMessage']?.currentValue !== undefined) {
      setTimeout(() => this.messagesPanel.nativeElement.focus(), 0);
    }
  }

  toggleEmojiPanel(event: MouseEvent) {
    event.stopPropagation();
    this.showEmojis = !this.showEmojis;
  }

  onSelectEmojis(emojiSelected: any) {
    const emoji: EmojiData = emojiSelected.emoji;
    if (this.messageContent) {
      const firstChar = this.messageContent.charAt(0);
      if (/[\u0600-\u06FF]/.test(firstChar)) {
        this.messageContent = emoji.native + this.messageContent;
      } else {
        this.messageContent += emoji.native;
      }
    } else {
      this.messageContent = emoji.native;
    }
  }

  @HostListener('document:click', ['$event'])
  handleClickOutside(event: MouseEvent) {
    if (this.showEmojis && this.emojiPanel && !this.emojiPanel.nativeElement.contains(event.target)) {
      this.showEmojis = false;
    }
  }

  keyDown(event: KeyboardEvent) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  onClick() {
    this.setMessagesToSeen();
  }

  private setMessagesToSeen() {
    // this.messageService.setMessagesToSeen({
    //   chatId: this.selectedChat.id as string,
    // }).subscribe({
    //   next: () => {}
    // });
  }

  uploadMedia(event: Event) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {
      const files: FileList = input.files;
      this.sendMediaMessage(files);
      this.repliedMessage.set(undefined);
    }
  }

  sendMediaMessage(files: FileList) {
    const fileArray = Array.from(files);

    const repliedMessageSnapshot = this.repliedMessage();
    const isReplyMessageSnapshot = this.isReplyMessage;

    for (let i = 0; i < fileArray.length; i++) {
      const messageRequest: MessageCreationResource = {
        chatId: this.selectedChat().id as string,
        messageType: "MEDIA",
        mediaFiles: [fileArray[i] as Blob],
        repliedMessageId: isReplyMessageSnapshot && i === 0 ? repliedMessageSnapshot?.id : undefined,
      };

      this.messageService.saveMessage({ body: messageRequest }).subscribe(res => {
        const response = res!.body!;

        const message: MessageResponse = {
          id: response.id,
          senderId: this.keycloakService.userId as string,
          type: "MEDIA",
          mediaListReferences: response.mediaListReferences,
          state: "SENT",
          createdAt: new Date().toISOString(),
          repliedMessage: isReplyMessageSnapshot && i === 0
            ? {
              id: repliedMessageSnapshot?.id,
              senderId: repliedMessageSnapshot?.senderId,
              messageType: repliedMessageSnapshot?.type,
              content: repliedMessageSnapshot?.type === "TEXT"
                ? { content: repliedMessageSnapshot?.content }
                : { mediaReferences: repliedMessageSnapshot?.mediaListReferences },
            } as RepliedMessage
            : undefined,
        };

        this.messageCreated.emit(message);
        this.messageContent = '';
        this.showEmojis = false;

        if (i === fileArray.length-1) {
          this.repliedMessage.set(undefined);
        }
      });
    }
  }

  sendMessage() {
    if (this.messageContent && this.messageContent.length > 0) {
      const messageRequest: MessageCreationResource = {
        chatId: this.selectedChat().id as string,
        content: this.messageContent,
        messageType: "TEXT",
        repliedMessageId: this.isReplyMessage? this.repliedMessage()?.id : undefined,
      };
      this.messageService.saveMessage({
        body: messageRequest
      }).subscribe({
        next: res => {
          const repliedMessage = this.repliedMessage();
          const message: MessageResponse = {
            id: res.body?.id,
            senderId: this.keycloakService.userId as string,
            content: this.messageContent,
            type: "TEXT",
            state: "SENT",
            createdAt: new Date().toISOString(),
            repliedMessage: this.isReplyMessage? {
              id: repliedMessage?.id,
              senderId: repliedMessage?.senderId,
              messageType: repliedMessage?.type,
              content: repliedMessage?.type === "TEXT"? {content: repliedMessage?.content}
                : {mediaReferences: repliedMessage?.mediaListReferences},
            } as RepliedMessage : undefined,
          };
          this.messageCreated.emit(message);
          this.repliedMessage.set(undefined);
          this.messageContent = '';
          this.showEmojis = false;
        }
      })
    }
  }

  autoResize(event: Event): void {
    const textarea = event.target as HTMLTextAreaElement;
    textarea.style.height = "auto";
    textarea.style.height = Math.min(textarea.scrollHeight, textarea.scrollHeight) + "px";
  }

  get isReplyMessage() {
    return this.repliedMessage() !== undefined;
  }

  get replyMessage() {
    const repliedMessage = this.repliedMessage();
    return {
      senderId: repliedMessage?.senderId,
      senderName: repliedMessage?.senderId as string === this.keycloakService.userId as string?
        'You': this.chatUsers()!.filter(user => user.id === repliedMessage?.senderId as string)
          .map(user => user.fullname),
      type: repliedMessage?.type,
      content: repliedMessage?.content
    }
  }

  cancelReply() {
    this.repliedMessage.set(undefined);
  }
}
