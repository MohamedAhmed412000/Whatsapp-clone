import {
  AfterViewChecked,
  AfterViewInit,
  Component,
  computed, effect,
  ElementRef, EventEmitter,
  input, Output,
  signal,
  ViewChild
} from '@angular/core';
import {AsyncPipe, DatePipe, NgStyle} from '@angular/common';
import {KeycloakService} from '../../../utils/keycloak/keycloak.service';
import {ChatMessageResponse} from '../../../services/core/models/chat-message-response';
import {MessageResponse} from '../../../services/core/models/message-response';
import {ChatResponse} from '../../../services/core/models/chat-response';
import {ChatUserResponse} from '../../../services/core/models/chat-user-response';
import {RelativeChatDatePipe} from '../../../utils/relative-messages-date.pipe';
import {MediaUrlPipe} from '../../../utils/media/media-url.pipe';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {MediaService} from '../../../utils/media/media.service.';
import {ColorizePipe} from '../../../utils/colorize.pipe';
import {GalleryService} from '../../../shared/gallery.service';

@Component({
  selector: 'app-conversation-messages',
  imports: [
    DatePipe,
    RelativeChatDatePipe,
    MediaUrlPipe,
    FaIconComponent,
    NgStyle,
    ColorizePipe,
    AsyncPipe
  ],
  templateUrl: './conversation-messages.html',
  styleUrl: './conversation-messages.scss'
})
export class ConversationMessages implements AfterViewInit, AfterViewChecked {
  chat = input<ChatResponse>();
  chatMessages = input<ChatMessageResponse>();
  chatUsers = input<ChatUserResponse[]>();
  chatUsersMap = computed(() => {
    const users = this.chatUsers();
    if (users && users.length > 0) {
      return new Map(users!.map(user => [user.id, user]));
    }
    return new Map();
  });
  showContextMenu = signal(false);
  contextMenuPosition = signal<{ x: string; y: string }>({ x: '0px', y: '0px' });
  @ViewChild('scrollableDiv', { static: false }) private scrollableDiv?: ElementRef<HTMLDivElement>;

  protected repliedMessage: MessageResponse | undefined;
  protected playingMessageId: number | undefined;
  protected audioProgressMap = new Map<number, number>();
  protected durationMap = new Map<number, number>();
  private animationFrame?: number;

  @Output() chatUserSelected = new EventEmitter<ChatUserResponse>();
  @Output() repliedMessageEvent = new EventEmitter<MessageResponse>();

  constructor(
    private keycloakService: KeycloakService,
    private mediaService: MediaService,
    private galleryService: GalleryService,
  ) {
    document.addEventListener('click', () => this.closeContextMenu());
    effect(() => {
      if (this.chat() && this.scrollableDiv) {
        this.scrollToBottom();
      }
    });
  }

  ngAfterViewInit(): void {
    // this.scrollToBottom();
    //
    // const el = this.scrollableDiv!.nativeElement;
    // el.addEventListener('scroll', () => {
    //   if (el.scrollTop === 0) {
    //     console.log("Load next page")
    //     // this.loadOlderMessages();
    //   }
    // });
  }

  ngAfterViewChecked() {
    if (this.scrollableDiv) {
      // console.log("Scroll to bottom");
      this.scrollToBottom();
    }

    const messages = Object.values(this.chatMessages() ?? {}).flat();
    for (const msg of messages) {
      if (msg.type === 'AUDIO') {
        this.registerAudioMetadataListener(msg);
      }
    }
  }

  private scrollToBottom(): void {
    if (!this.scrollableDiv?.nativeElement) return;
    const scrollContainer = this.scrollableDiv.nativeElement;
    scrollContainer.scrollTop = 0;
  }

  onRightClick(event: MouseEvent, message: MessageResponse) {
    event.preventDefault();

    const menuWidth = 180;
    const menuHeight = 350;
    const viewportWidth = window.innerWidth;
    const viewportHeight = window.innerHeight;
    let posX = event.clientX;
    let posY = event.clientY;
    if (posX + menuWidth > viewportWidth) {
      posX = posX - menuWidth;
    }
    if (posY + menuHeight > viewportHeight) {
      posY = posY - menuHeight;
    }

    this.showContextMenu.set(true);
    this.contextMenuPosition.set({
      x: `${posX}px`,
      y: `${posY}px`
    });
    this.repliedMessage = message;
  }

  closeContextMenu() {
    this.showContextMenu.set(false);
  }

  getSortedDates() {
    const map = this.chatMessages();
    if (!map) return [];
    return Object.keys(map!);
  }

  getSortedMessagesForDate(date: string): MessageResponse[] {
    const map = this.chatMessages();
    return (map?.[date] ?? []);
  }

  getChatUserFullName(userId: string): string {
    if (userId === this.keycloakService.userId) return "You";
    const chatUser = this.chatUsersMap().get(userId);
    return `${chatUser?.firstname} ${chatUser?.lastname}`;
  }

  isSelfMessage(message: MessageResponse) {
    return message.senderId === this.keycloakService.userId;
  }

  isSelfRepliedMessage(message: MessageResponse) {
    return message.repliedMessage!.senderId === this.keycloakService.userId;
  }

  isSelfChat() {
    return this.chat()?.receiversId!.every(userId => userId === this.keycloakService.userId);
  }

  replyMessage() {
    this.repliedMessageEvent.emit(this.repliedMessage);
  }

  async copyMessage() {
    if (this.repliedMessage!.type === 'TEXT') {
      await navigator.clipboard.writeText(this.repliedMessage!.content ?? '');
    } else if (this.repliedMessage!.type === 'MEDIA' && this.repliedMessage!.mediaListReferences?.length) {
      const url = this.mediaService.generateUrl(this.repliedMessage!.mediaListReferences[0]);
      const response = await fetch(url, { mode: 'cors' });
      const blob = await response.blob();

      const bitmap = await createImageBitmap(blob);
      const canvas = document.createElement("canvas");
      canvas.width = bitmap.width;
      canvas.height = bitmap.height;
      const ctx = canvas.getContext("2d")!;
      ctx.drawImage(bitmap, 0, 0);
      const pngBlob = await new Promise<Blob>((resolve) =>
        canvas.toBlob((b) => resolve(b!), "image/png")
      );

      const item = new ClipboardItem({ "image/png": pngBlob });
      await navigator.clipboard.write([item]);
    }

    this.closeContextMenu();
  }

  onChatUserSelected(userId: String): void {
    this.chatUserSelected.emit(this.chatUsersMap().get(userId));
  }

  scrollToRepliedMessage(repliedMessageId?: number | string) {
    if (!repliedMessageId) return;

    setTimeout(() => {
      const targetElement = document.getElementById(`message-${repliedMessageId}`);
      if (targetElement) {
        targetElement.scrollIntoView({
          behavior: 'smooth',
          block: 'center',
        });

        targetElement.classList.add('highlight-message');
        setTimeout(() => targetElement.classList.remove('highlight-message'), 800);
      }
    }, 0);
  }

  togglePlay(message: MessageResponse) {
    const audio = document.querySelector(`#audio-${message.id}`) as HTMLAudioElement;
    if (!audio) return;

    if (this.playingMessageId === message.id) {
      audio.pause();
      this.playingMessageId = undefined;
      cancelAnimationFrame(this.animationFrame!);
      return;
    }

    if (this.playingMessageId && this.playingMessageId !== message.id) {
      const prev = document.querySelector(`#audio-${this.playingMessageId}`) as HTMLAudioElement;
      if (prev) prev.pause();
    }
    this.playingMessageId = message.id;
    this.updateDuration(message, audio);

    audio.play();
    const update = () => {
      if (!this.playingMessageId || audio.paused) return;
      const progress = audio.currentTime / audio.duration;
      this.audioProgressMap.set(message.id!, progress);
      const remaining = Math.max(0, Math.floor(audio.duration - audio.currentTime));
      this.durationMap.set(message.id!, remaining);
      this.animationFrame = requestAnimationFrame(update);
    };
    update();

    audio.onended = () => {
      this.playingMessageId = undefined;
      this.audioProgressMap.set(message.id!, 0);
      const totalDuration = Math.floor(audio.duration);
      this.durationMap.set(message.id!, totalDuration);
      cancelAnimationFrame(this.animationFrame!);
    };
  }

  registerAudioMetadataListener(message: MessageResponse) {
    if (message.type === 'AUDIO') {
      const audio = document.querySelector(`#audio-${message.id}`) as HTMLAudioElement;
      if (!audio) return;
      if ((audio as any)._durationBound) return;
      (audio as any)._durationBound = true;

      audio.addEventListener('loadedmetadata', () => {
        const totalDuration = Math.floor(audio.duration);
        this.durationMap.set(message.id!, totalDuration);
      });
    }
  }

  isPlaying(message: any) {
    return this.playingMessageId === message.id;
  }

  getRemainingDuration(message: MessageResponse): string {
    const sec = this.durationMap.get(message.id!) ?? 0;
    const m = Math.floor(sec / 60);
    const s = sec % 60;
    return `${m}:${s < 10 ? '0' + s : s}`;
  }

  getProgress(message: MessageResponse): number {
    return this.audioProgressMap.get(message.id!) ?? 0;
  }

  updateDuration(message: MessageResponse, audio: HTMLAudioElement) {
    if (isNaN(audio.duration)) {
      audio.onloadedmetadata = () => {
        this.durationMap.set(message.id!, Math.floor(audio.duration));
      };
    } else {
      this.durationMap.set(message.id!, Math.floor(audio.duration));
    }
  }

  onSeek(event: MouseEvent, message: MessageResponse) {
    const audio = document.querySelector(`#audio-${message.id}`) as HTMLAudioElement;
    if (!audio || isNaN(audio.duration)) return;

    const waveform = event.currentTarget as HTMLElement;
    const rect = waveform.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const progress = Math.min(Math.max(x / rect.width, 0), 1);

    audio.currentTime = progress * audio.duration;
    this.audioProgressMap.set(message.id!, progress);
  }

  onImageClick(message: MessageResponse, index: number) {
    const images = message.mediaListReferences!.map((ref: string) => ({
      src: ref,
      caption: this.getChatUserFullName(message.senderId!)
    }));

    this.galleryService.open(images, index);
  }
}
