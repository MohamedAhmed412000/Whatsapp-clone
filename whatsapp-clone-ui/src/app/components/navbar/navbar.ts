import { Component } from '@angular/core';
import {KeycloakService} from '../../utils/keycloak/keycloak.service';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {NgbDropdown, NgbDropdownItem, NgbDropdownMenu, NgbDropdownToggle} from '@ng-bootstrap/ng-bootstrap';

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

  }
}
