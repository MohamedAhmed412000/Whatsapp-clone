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

@Component({
  selector: 'app-navbar',
  imports: [
    FaIconComponent,
    NgbDropdown,
    NgbDropdownToggle,
    NgbDropdownMenu,
    NgbDropdownItem,
    MediaUrlPipe
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss'
})
export class Navbar {
  offCanvasService = inject(NgbOffcanvas);
  offcanvasTracker = inject(OffcanvasTrackerService);

  @Output() userChatSelected = new EventEmitter<UserResponse>();

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

    this.offcanvasTracker.register(ref);
  }

  protected readonly console = console;
}
