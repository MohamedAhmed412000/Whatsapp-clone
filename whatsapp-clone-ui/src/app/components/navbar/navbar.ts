import {Component, inject} from '@angular/core';
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

@Component({
  selector: 'app-navbar',
  imports: [
    FaIconComponent,
    NgbDropdown,
    NgbDropdownToggle,
    NgbDropdownMenu,
    NgbDropdownItem
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss'
})
export class Navbar {

  offCanvasService = inject(NgbOffcanvas);

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
    this.offCanvasService.open(NewConversation, {
      position: "start",
      container: "#main",
      panelClass: "offcanvas",
    })
  }
}
