import {Component, EventEmitter, inject, Output} from '@angular/core';
import {KeycloakService} from '../../utils/keycloak/keycloak.service';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {
  NgbDropdown,
  NgbDropdownItem,
  NgbDropdownMenu,
  NgbDropdownToggle,
  NgbOffcanvas
} from '@ng-bootstrap/ng-bootstrap';
import {NewConversation} from './new-conversation/new-conversation';
import {OffcanvasTrackerService} from '../../utils/offCanvasStack/offcanvas-tracker.service';
import {MediaUrlPipe} from '../../utils/media/media-url.pipe';
import {UserResponse} from '../../services/user/models/user-response';
import {AsyncPipe} from '@angular/common';
import {ChatResponse} from '../../services/core/models/chat-response';
import {NewGroupChat} from './new-conversation/new-group-chat/new-group-chat';

@Component({
  selector: 'app-navbar',
  imports: [
    FaIconComponent,
    NgbDropdown,
    NgbDropdownToggle,
    NgbDropdownMenu,
    NgbDropdownItem,
    MediaUrlPipe,
    AsyncPipe
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss'
})
export class Navbar {
  offCanvasService = inject(NgbOffcanvas);
  offcanvasTracker = inject(OffcanvasTrackerService);

  @Output() userChatSelected = new EventEmitter<UserResponse>();
  @Output() groupChatCreated = new EventEmitter<ChatResponse>();

  constructor(
    protected keycloakService: KeycloakService
  ) {}

  logout(): void {
    this.keycloakService.logout();
  }

  editProfile(): void {
    this.keycloakService.accountManagement();
  }

  openNewConversations() {
    const ref = this.offCanvasService.open(NewConversation, {
      position: "start",
      container: "#main",
      panelClass: "offcanvas",
    });
    (ref.componentInstance as NewConversation).userChatSelected.subscribe((user: UserResponse) => {
      this.userChatSelected.emit(user);
    });
    (ref.componentInstance as NewConversation).groupChatCreated.subscribe((chat: ChatResponse) => {
      this.groupChatCreated.emit(chat);
    });

    this.offcanvasTracker.register(ref);
  }

  protected readonly console = console;
}
