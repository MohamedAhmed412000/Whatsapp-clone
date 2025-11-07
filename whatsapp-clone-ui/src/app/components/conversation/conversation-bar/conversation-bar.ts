import {Component, HostListener, input} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {ChatResponse} from '../../../services/core/models/chat-response';
import {MediaUrlPipe} from '../../../utils/media/media-url.pipe';
import {KeycloakService} from '../../../utils/keycloak/keycloak.service';
import {ChatUserResponse} from '../../../services/core/models/chat-user-response';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {EditContactModal} from './edit-contact-modal/edit-contact-modal';
import {AsyncPipe} from '@angular/common';
import {UserResponse} from '../../../services/user/models/user-response';
import {UsersControllerService} from '../../../services/user/services/users-controller.service';

@Component({
  selector: 'app-conversation-bar',
  imports: [
    FaIconComponent,
    MediaUrlPipe,
    AsyncPipe
  ],
  templateUrl: './conversation-bar.html',
  styleUrl: './conversation-bar.scss'
})
export class ConversationBar {
  chat = input.required<ChatResponse>();
  chatUsers = input.required<ChatUserResponse[]>();

  protected showProfile: boolean = false;
  protected selectedTab = 'overview';
  protected profileTabs = [
    { id: 'overview', label: 'Overview', icon: 'user' },
    { id: 'members', label: 'Members', icon: 'user-group' },
    { id: 'media', label: 'Media', icon: 'image' },
    { id: 'files', label: 'Files', icon: 'file' },
    { id: 'links', label: 'Links', icon: 'link' },
    { id: 'events', label: 'Events', icon: 'calendar' },
    { id: 'encryption', label: 'Encryption', icon: 'lock' }
  ];
  protected isAddingChatMembers: boolean = false;
  protected chatUserContacts: UserResponse[] = [];

  constructor(
    private userService: UsersControllerService,
    private keycloakService: KeycloakService,
    private modalService: NgbModal
  ) {
    this.isAddingChatMembers = false;
  }

  isSelfChat(): boolean {
    return this.chat()?.receiversId!.every(userId => userId === this.keycloakService.userId);
  }

  isSelfChatUser(userId: string): boolean {
    return userId === this.keycloakService.userId;
  }

  @HostListener('document:click')
  onOutsideClick() {
    if (this.showProfile) {
      this.showProfile = false;
    }
  }

  openProfile(event: MouseEvent) {
    event.stopPropagation();
    this.showProfile = !this.showProfile;
  }

  selectTab(tab: string) {
    this.selectedTab = tab;
  }

  get singleChatUser() {
    return this.chatUsers()
      ?.filter(chatUser => chatUser.id !== this.keycloakService.userId)
      ?.at(0)!;
  }

  get chatUsersString() {
    return this.chatUsers()
      ?.filter(chatUser => chatUser.id != this.keycloakService.userId)
      ?.map(chatUser => chatUser.firstname)
      ?.join(', ');
  }

  get me() {
    return this.chatUsers()
      ?.filter(chatUser => chatUser.id === this.keycloakService.userId)
      ?.at(0)!;
  }

  stopMainEffect(event: MouseEvent) {
    event.stopPropagation();
  }

  openEditContactModal(): void {
    const modalRef = this.modalService.open(EditContactModal, {
      centered: true
    });

    if (this.isSelfChat()) {
      modalRef.componentInstance.chatUser = {
        id: this.keycloakService.userId,
        firstname: this.keycloakService.me.firstName,
        lastname: this.keycloakService.me.lastName,
        fullname: this.keycloakService.me.fullName,
        description: this.keycloakService.me.description,
        email: this.keycloakService.me.email,
        online: true,
        imageFileReference: this.keycloakService.me.profilePictureReference,
        admin: true
      } as ChatUserResponse;
    } else {
      modalRef.componentInstance.chatUser = this.singleChatUser;
    }
    modalRef.componentInstance.chat = structuredClone(this.chat());
  }

  addChatMember() {
    this.isAddingChatMembers = true;
    this.userService.getUsers().subscribe({
      next: (res) => {
        const chatUserIds = new Set(this.chatUsers().map(user => user.id));
        this.chatUserContacts = res.body?.filter(user => !chatUserIds.has(user.id)) ?? [];
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  returnToMembersView() {
    this.isAddingChatMembers = false;
  }
}
