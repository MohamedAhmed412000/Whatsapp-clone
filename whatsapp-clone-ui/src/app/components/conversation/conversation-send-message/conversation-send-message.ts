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
import {NgClass} from '@angular/common';

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
    ColorizePipe,
    NgClass,
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
  isRecording = false;
  mediaRecorder!: MediaRecorder;
  recordedChunks: BlobPart[] = [];
  recordingStartTime!: number;
  audioBlob!: Blob;
  audioPreviewUrl: string | null = null;
  dots = Array(30).fill(0);
  recordingDisplayTime = '0:00';
  audioContext?: AudioContext;
  isInitializingRecording = false;
  isCancellingRecording = false;
  timerInterval?: any;
  audioElement?: HTMLAudioElement;
  isAudioPlaying = false;
  audioProgress = 0;
  audioDurationDisplay = '0:00';
  progressInterval?: any;

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

  onMicClick() {
    if (!this.isRecording) {
      this.audioPreviewUrl = null;
      this.startRecording();
    } else {
      this.stopRecording();
    }
  }

  private startRecording() {
    if (this.isRecording || this.isInitializingRecording) return;

    this.isInitializingRecording = true;
    this.isCancellingRecording = false;
    navigator.mediaDevices.getUserMedia({ audio: true })
      .then((stream) => {
        this.audioContext = new AudioContext();
        this.mediaRecorder = new MediaRecorder(stream);
        this.recordedChunks = [];

        this.isRecording = true;
        this.audioPreviewUrl = null;
        this.recordingStartTime = Date.now();
        this.recordingDisplayTime = '0:00';
        this.mediaRecorder.ondataavailable = (event) => {
          if (event.data.size > 0) this.recordedChunks.push(event.data);
        };

        this.mediaRecorder.onstop = () => {
          clearInterval(this.timerInterval);
          this.isRecording = false;
          this.isInitializingRecording = false;

          if (this.audioContext) this.audioContext.close().then();

          if (this.audioPreviewUrl) {
            URL.revokeObjectURL(this.audioPreviewUrl);
            this.audioPreviewUrl = null;
          }

          if (this.isCancellingRecording) {
            this.isCancellingRecording = false;
            this.recordedChunks = [];
            this.audioBlob = new Blob();
            this.resetAudioPlayer();
            return;
          }

          const audioBlob = new Blob(this.recordedChunks, { type: 'audio/webm' });
          this.audioBlob = audioBlob;
          this.audioPreviewUrl = URL.createObjectURL(audioBlob);
        };

        this.resetAudioPlayer();

        this.timerInterval = setInterval(() => {
          const elapsed = Date.now() - this.recordingStartTime;
          this.recordingDisplayTime = this.formatTime(elapsed);
        }, 200);

        this.mediaRecorder.start();
        this.isInitializingRecording = false;
      })
      .catch((err) => {
        console.error('Error accessing microphone:', err);
        this.isInitializingRecording = false;
        alert('Microphone access denied.');
      });
  }

  cancelRecording() {
    if (this.mediaRecorder && this.isRecording) {
      this.isCancellingRecording = true;
      this.mediaRecorder.stop();
    }
    clearInterval(this.timerInterval);

    if (this.audioPreviewUrl) {
      URL.revokeObjectURL(this.audioPreviewUrl);
      this.audioPreviewUrl = null;
    }
    this.resetAudioPlayer();

    this.isRecording = false;
    this.recordingDisplayTime = '0:00';
    this.recordedChunks = [];
    this.audioBlob = new Blob();
  }

  private resetAudioPlayer() {
    if (this.audioElement) {
      this.audioElement.pause();
      this.audioElement.src = '';
      this.audioElement.load();
    }

    this.audioElement = undefined;
    this.isAudioPlaying = false;
    this.audioProgress = 0;
    this.audioDurationDisplay = '0:00';
    clearInterval(this.progressInterval);
  }

  private formatTime(ms: number): string {
    const totalSeconds = Math.floor(ms / 1000);
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
  }

  stopRecording() {
    if (this.mediaRecorder && this.isRecording) {
      this.mediaRecorder.stop();
    }
  }

  togglePlay() {
    if (!this.audioElement) {
      this.audioElement = new Audio(this.audioPreviewUrl!);
      this.audioElement.addEventListener('loadedmetadata', () => {
        this.audioDurationDisplay = this.formatTime(this.audioElement!.duration * 1000);
      });

      this.audioElement.addEventListener('ended', () => {
        this.isAudioPlaying = false;
        this.audioProgress = 100;
        clearInterval(this.progressInterval);
      });
    }

    if (this.isAudioPlaying) {
      this.audioElement?.pause();
      this.isAudioPlaying = false;
      clearInterval(this.progressInterval);
    } else {
      this.audioElement?.play();
      this.isAudioPlaying = true;
      this.startProgressTracking();
    }
  }

  startProgressTracking() {
    clearInterval(this.progressInterval);
    this.progressInterval = setInterval(() => {
      if (this.audioElement && this.audioElement.duration > 0) {
        this.audioProgress = (this.audioElement.currentTime / this.audioElement.duration) * 100;
      }
    }, 200);
  }

  resumeRecording() {
    this.cancelRecording();
    this.startRecording();
  }

  sendRecording() {
    if (!this.audioBlob) return;

    const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
    const audioFileName = `audio_${timestamp}.webm`;
    const audioFile = new File([this.audioBlob], audioFileName, { type: 'audio/webm' });

    const messageRequest: MessageCreationResource = {
      chatId: this.selectedChat().id as string,
      messageType: 'AUDIO',
      mediaFiles: [audioFile],
      repliedMessageId: this.isReplyMessage? this.repliedMessage()?.id : undefined,
    };

    this.messageService.saveMessage({ body: messageRequest }).subscribe({
      next: res => {
        const response = res!.body!;
        const message: MessageResponse = {
          id: response.id,
          senderId: this.keycloakService.userId as string,
          type: "AUDIO",
          mediaListReferences: response.mediaListReferences,
          state: "SENT",
          createdAt: new Date().toISOString(),
        };
        this.messageCreated.emit(message);
        this.cancelRecording();
      },
      error: err => {
        console.error('Audio upload failed:', err);
      }
    });
  }
}
